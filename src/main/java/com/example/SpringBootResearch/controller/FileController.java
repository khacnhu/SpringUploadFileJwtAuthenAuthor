package com.example.SpringBootResearch.controller;


import com.example.SpringBootResearch.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file/")
public class FileController {

    private final FileService fileService;


    public FileController(FileService fileService) {
        this.fileService= fileService;
    }

    @Value("${project.poster}")
    private String path;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFileHandler(@RequestPart MultipartFile file) throws IOException {
        String uploadFileName = fileService.uploadFile(path, file);
        return ResponseEntity.ok("File uploaded : " + uploadFileName);
    }

    @GetMapping("/{fileName}")
    public void serverFileHandler(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        InputStream resourceFile = fileService.getResourceFile(path, fileName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resourceFile, response.getOutputStream());
    }


}
