package com.github.bernabaris.repsyapp.service;

import com.github.bernabaris.repsyapp.entity.PackageEntity;
import com.github.bernabaris.repsyapp.repository.PackageRepository;
import com.github.bernabaris.repsyapp.util.CustomMultipartFile;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@ConditionalOnProperty(value = "storage.strategy", havingValue = "object-storage")
@Slf4j
public class ObjectStorageService implements StorageService {

    @Autowired
    private PackageRepository packageRepository;

    @PostConstruct
    public void init() {
        log.info("{} is started.", this.getClass().getSimpleName());
    }

    @Override
    public void writeFile(String packageName, String version, MultipartFile file) throws IOException {

        PackageEntity packageEntity = packageRepository.getPackage(packageName, version, file.getOriginalFilename());
        if (packageEntity == null) {
            // new file creation
            packageEntity = new PackageEntity();
            packageEntity.setPackageName(packageName);
            packageEntity.setVersion(version);
            packageEntity.setFileName(file.getOriginalFilename());
            packageEntity.setData(file.getBytes());
        } else {
            // overwrite file
            packageEntity.setData(file.getBytes());
        }
        packageRepository.save(packageEntity);
    }

    @Override
    public MultipartFile readFile(String packageName, String version, String fileName) {
        PackageEntity packageEntity = packageRepository.getPackage(packageName, version, fileName);
        if (packageEntity == null) {
            return null;
        }
        return new CustomMultipartFile(packageEntity.getData(), packageEntity.getFileName());
    }
}
