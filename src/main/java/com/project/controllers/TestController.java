package com.project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestPart("file") MultipartFile file, @RequestPart("a") String object) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        return ResponseEntity.ok().body("File uploaded successfully");
    }
}

