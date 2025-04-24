package com.github.bernabaris.repsyapp.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void writeFile(String packageName, String version,MultipartFile file);
    MultipartFile readFile(String packageName, String version, String fileName);
}
