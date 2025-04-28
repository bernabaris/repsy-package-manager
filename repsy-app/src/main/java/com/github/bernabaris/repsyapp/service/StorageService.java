package com.github.bernabaris.repsyapp.service;

import com.github.bernabaris.filesystemstorage.FileSystemStorage;
import com.github.bernabaris.objectstorage.ObjectStorage;
import com.github.bernabaris.repsyapp.entity.PackageEntity;
import com.github.bernabaris.repsyapp.entity.StorageType;
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

    private StorageType storageType;

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
            storageType = StorageType.FILE_SYSTEM;
            fileSystemStorage = new FileSystemStorage();
            log.info("File-System enabled.");
        } else {
            storageType = StorageType.OBJECT_STORAGE;
            objectStorage = new ObjectStorage(minioEndpoint, minioUser, minioPassword, minioBucketName);
            log.info("Object-Storage enabled.");
        }
        log.info("{} is started.", this.getClass().getSimpleName());
    }

    public boolean writeFile(String packageName, String version, MultipartFile file) {
        String path;
        try {
            PackageEntity packageEntity = packageRepository.getPackage(packageName, version, file.getOriginalFilename(),
                    storageType);
            switch (storageType) {
                case FILE_SYSTEM -> path = String.format("%s/%s/%s/%s/%s",
                        storageDirectory, REPO_FOLDER_NAME, packageName, version, file.getOriginalFilename());
                case OBJECT_STORAGE ->  path = String.format("%s/%s/%s", packageName, version, file.getOriginalFilename());
                default -> {
                    log.error("Unhandled storage type: {}",storageType);
                    return false;
                }
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
            switch (storageType) {
                case FILE_SYSTEM -> fileSystemStorage.writeFile(path, file.getBytes());
                case OBJECT_STORAGE -> objectStorage.storeFile(path, file.getInputStream());
                default -> {
                    log.error("Unhandled storage type: {}",storageType);
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while writing file {}", file.getOriginalFilename(), e);
            return false;
        }
        return true;
    }

    public MultipartFile readFile(String packageName, String version, String fileName) {
        PackageEntity packageEntity = packageRepository.getPackage(packageName, version, fileName,storageType);
        if (packageEntity == null) {
            return null;
        }
        byte[] fileBytes;
        try {
            switch (storageType) {
                case FILE_SYSTEM -> fileBytes = fileSystemStorage.readFile(packageEntity.getPath());
                case OBJECT_STORAGE -> {
                    InputStream is = objectStorage.retrieveFile(packageEntity.getPath());
                    fileBytes = is.readAllBytes();
                }
                default -> {
                    log.error("Unhandled storage type: {}",storageType);
                    return null;
                }
            }
            return new CustomMultipartFile(fileBytes, fileName);
        } catch (Exception e) {
            log.error("Exception occurred while reading file: {}", packageEntity.getPath(), e);
            return null;
        }
    }
}
