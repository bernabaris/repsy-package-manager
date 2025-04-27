package com.github.bernabaris.filesystemstorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemStorage {

    public void writeFile(String destPath, byte[] data) throws IOException {
        File file = new File(destPath);
        File folder = new File(file.getParent());
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (!created) {
                throw new FileSystemException(String.format("Failed to create directory: %s", folder.getAbsolutePath()));
            }
        }
        Path path = Paths.get(destPath);
        Files.write(path, data);
    }

    public byte[] readFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("File not found: %s", filePath));
        }
        return Files.readAllBytes(Paths.get(filePath));
    }
}
