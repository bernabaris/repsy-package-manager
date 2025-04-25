package com.github.bernabaris.repsyapp.service;

import com.github.bernabaris.repsyapp.util.CustomMultipartFile;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@ConditionalOnProperty(value="storage.strategy", havingValue = "file-system")
@Slf4j
public class FileSystemService implements StorageService{

    private static final String REPO_FOLDER_NAME = "repository";

    @Value("${storage.fs.path}")
    private String storageDirectory;

    @PostConstruct
    public void init(){
        log.info("{} is started.", this.getClass().getSimpleName());
    }


    @Override
    public void writeFile(String packageName, String version, MultipartFile file) throws IOException {
        try {
            String packageDir = String.format("%s/%s/%s/%s", storageDirectory, REPO_FOLDER_NAME, packageName, version);
            File dir = new File(packageDir);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (created) {
                    log.info("Created directory: {}", packageDir);
                } else {
                    log.warn("Failed to create directory: {}", packageDir);
                }
            }

            File dest = new File(packageDir + "/" + file.getOriginalFilename());
            file.transferTo(dest);
            log.info("File {} saved at {}", file.getOriginalFilename(), dest.getAbsolutePath());

        } catch (IOException e) {
            log.error("Failed to save file: {}", file.getOriginalFilename(), e);
            throw e;
        }

    }

    @Override
    public MultipartFile readFile(String packageName, String version, String fileName) {
        try {
            String filePath = String.format("%s/%s/%s/%s/%s",
                    storageDirectory, REPO_FOLDER_NAME, packageName, version, fileName);
            File file = new File(filePath);
            if (!file.exists()) {
                log.error("File not found: {}", filePath);
                return null;
            }

            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            return new CustomMultipartFile(fileBytes, fileName);

        } catch (IOException e) {
            log.error("Failed to read file: {}", fileName, e);
            return null;
        }
    }
}
