package com.github.bernabaris.repsyapp.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@ConditionalOnProperty(value="storage.strategy", havingValue = "object-storage")
@Slf4j
public class ObjectStorageService implements StorageService{

    @PostConstruct
    public void init(){
        log.info("{} is started.", this.getClass().getSimpleName());
    }

    @Override
    public void writeFile(String packageName, String version, MultipartFile file) {

    }

    @Override
    public MultipartFile readFile(String packageName, String version, String fileName) {
        return null;
    }
}
