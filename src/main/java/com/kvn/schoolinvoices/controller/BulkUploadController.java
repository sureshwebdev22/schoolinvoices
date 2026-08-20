package com.kvn.schoolinvoices.controller;

import com.kvn.schoolinvoices.service.BulkUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/schooladmin")
public class BulkUploadController {

    private static final Logger logger = LoggerFactory.getLogger(BulkUploadController.class);

    @Autowired
    private  BulkUploadService service;



    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload(@RequestParam MultipartFile file) throws IOException {

        Path tempFile = Files.createTempFile("students-", ".csv");

        file.transferTo(tempFile);

        String jobId = UUID.randomUUID().toString();

        service.importCsv(tempFile.toFile(), jobId);

        logger.info("Student import completed for jobId: {}", jobId);
        return ResponseEntity.ok(Map.of("jobId", jobId));
    }

    @PostMapping("/upload/user")
    public ResponseEntity<Map<String, String>> uploadUser(@RequestParam MultipartFile file) throws IOException {
    
        logger.info("Starting user upload process for file: {}", file.getOriginalFilename());
        Path tempFile = Files.createTempFile("students-", ".csv");

        file.transferTo(tempFile);
        logger.debug("File transferred to temp location: {}", tempFile);

        String jobId = UUID.randomUUID().toString();
        logger.info("Generated jobId: {} for user upload", jobId);

        service.importCsv1(tempFile.toFile(), jobId);
        logger.info("User import completed for jobId: {}", jobId);

        return ResponseEntity.ok(Map.of("jobId", jobId));
    }
}
