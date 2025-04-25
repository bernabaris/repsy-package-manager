package com.github.bernabaris.repsyapp.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    void writeFile(String packageName, String version, MultipartFile file) throws IOException;
    MultipartFile readFile(String packageName, String version, String fileName) throws IOException;
}
