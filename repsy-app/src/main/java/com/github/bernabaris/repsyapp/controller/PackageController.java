package com.github.bernabaris.repsyapp.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bernabaris.repsyapp.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipInputStream;

@RestController
@Slf4j
public class PackageController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private ObjectMapper objectMapper;


    @PostMapping("/{packageName}/{version}")
    public ResponseEntity<?> deployPackage(@PathVariable String packageName,
                                           @PathVariable String version,
                                           @RequestParam("file") MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Uploaded file is missing or empty.");
        }
        String fileName = file.getOriginalFilename();

        if (!"meta.json".equals(fileName) && !"package.rep".equals(fileName)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Only 'package.rep' or 'meta.json' files are allowed.");
        }
        if (file.getOriginalFilename().equals("meta.json")) {
            try (InputStream is = file.getInputStream()) {
                JsonNode jsonNode = objectMapper.readTree(is);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("meta.json is not a valid JSON file.");
            }
        }

        if (file.getOriginalFilename().equals("package.rep")) {
            try (ZipInputStream zis = new ZipInputStream(file.getInputStream())) {
                if (zis.getNextEntry() == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("package.rep is not a valid zip file.");
                }
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("package.rep is not a valid zip file.");
            }
        }
        if (file.getOriginalFilename().equals("meta.json")) {
            try (InputStream is = file.getInputStream()) {
                JsonNode jsonNode = objectMapper.readTree(is);

                String name = jsonNode.path("name").asText(null);
                String versionValue = jsonNode.path("version").asText(null);
                String author = jsonNode.path("author").asText(null);

                if (name == null || name.isBlank() ||
                        versionValue == null || versionValue.isBlank() ||
                        author == null || author.isBlank()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Fields 'name', 'version', and 'author' must not be empty in meta.json.");
                }

                if (!name.equals(packageName) || !versionValue.equals(version)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Path parameters (packageName and version) do not match meta.json contents.");
                }

            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("meta.json is not a valid JSON file.");
            }
        }
        log.info("Deploying package: {} version: {}", packageName, version);
        storageService.writeFile(packageName,version,file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{packageName}/{version}/{fileName}")
    public ResponseEntity<?> downloadPackage(@PathVariable String packageName,
                                             @PathVariable String version,
                                             @PathVariable String fileName) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", fileName));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        MultipartFile file = storageService.readFile(packageName,version,fileName);
        if(file == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("File not found.");
        }
        ByteArrayResource resource = new ByteArrayResource(file.getBytes());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.getSize())
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(resource);
    }



}
