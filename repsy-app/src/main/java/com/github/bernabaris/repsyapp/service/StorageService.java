package com.github.bernabaris.repsyapp.service;

import com.github.bernabaris.filesystemstorage.FileSystemStorage;
import com.github.bernabaris.objectstorage.ObjectStorage;
import com.github.bernabaris.repsyapp.entity.PackageEntity;
import com.github.bernabaris.repsyapp.repository.PackageRepository;
import com.github.bernabaris.repsyapp.util.CustomMultipartFile;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;

@Service
@Slf4j
public class StorageService {

    private final PackageRepository packageRepository;

    public StorageService(@Autowired PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Value("${storage.strategy}")
    private String storageStrategy;

    private boolean fileSystemEnabled;

    private static final String REPO_FOLDER_NAME = "repository";

    @Value("${storage.fs.path}")
    private String storageDirectory;

    @Value("${minio.endpoint}")
    private String minioEndpoint;
    @Value("${minio.root.user}")
    private String minioUser;
    @Value("${minio.root.password}")
    private String minioPassword;
    @Value("${minio.default.buckets}")
    private String minioBucketName;

    FileSystemStorage fileSystemStorage;
    ObjectStorage objectStorage;

    @PostConstruct
    public void init() {
        if (Objects.equals(storageStrategy, "file-system")) {
            fileSystemEnabled = true;
            fileSystemStorage = new FileSystemStorage();
            log.info("File-System enabled.");
        } else {
            fileSystemEnabled = false;
            objectStorage = new ObjectStorage(minioEndpoint, minioUser, minioPassword, minioBucketName);
            log.info("Object-Storage enabled.");
        }
        log.info("{} is started.", this.getClass().getSimpleName());
    }

    public boolean writeFile(String packageName, String version, MultipartFile file) {
        String path;
        try {
            PackageEntity packageEntity = packageRepository.getPackage(packageName, version, file.getOriginalFilename());
            if (fileSystemEnabled) {
                path = String.format("%s/%s/%s/%s/%s",
                        storageDirectory, REPO_FOLDER_NAME, packageName, version, file.getOriginalFilename());
            } else {
                path = String.format("%s/%s/%s", packageName, version, file.getOriginalFilename());
            }
            if (packageEntity == null) {
                // new file creation
                packageEntity = new PackageEntity();
                packageEntity.setPackageName(packageName);
                packageEntity.setVersion(version);
                packageEntity.setFileName(file.getOriginalFilename());
                packageEntity.setPath(path);
                packageRepository.save(packageEntity);
            } else {
                path = packageEntity.getPath();
            }
            if (fileSystemEnabled) {
                fileSystemStorage.writeFile(path, file.getBytes());
            } else {
                objectStorage.storeFile(path, file.getInputStream());
            }
        } catch (Exception e) {
            log.error("Error occurred while writing file {}", file.getOriginalFilename(), e);
            return false;
        }
        return true;
    }

    public MultipartFile readFile(String packageName, String version, String fileName) {
        PackageEntity packageEntity = packageRepository.getPackage(packageName, version, fileName);
        if (packageEntity == null) {
            return null;
        }
        byte[] fileBytes;
        try {
            if (fileSystemEnabled) {
                fileBytes = fileSystemStorage.readFile(packageEntity.getPath());
            } else {
                InputStream is = objectStorage.retrieveFile(packageEntity.getPath());
                fileBytes = is.readAllBytes();
            }
            return new CustomMultipartFile(fileBytes, fileName);
        } catch (Exception e) {
            log.error("Exception occurred while reading file: {}", packageEntity.getPath(), e);
            return null;
        }
    }
}
