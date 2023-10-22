package com.example.AbnAmroTask.controller;

import com.example.AbnAmroTask.service.ProcessReportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private final ProcessReportService processReportService;

    @Value("${abnamrotask.outputfilename}")
    private String filename;

    public FileUploadController(ProcessReportService processReportService) {
        this.processReportService = processReportService;
    }

    @PostMapping(produces = "text/csv")
    public ResponseEntity<Resource> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .forEach(processReportService::transactionProcessor);

        File outputFile = processReportService.generateReport();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + filename)
                .contentLength(outputFile.length())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new FileSystemResource(outputFile));
    }
}

