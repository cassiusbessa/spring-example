package com.springboot.example.restful.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.springboot.example.restful.dto.v1.UploadFileResponseDTO;
import com.springboot.example.restful.services.FileStorageService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "File endpoints", description = "File API's for uploading and downloading files")
@RestController
@RequestMapping("/file")
public class FileController {

    private Logger logger = Logger.getLogger(FileController.class.getName());

    @Autowired  
    private FileStorageService service;

    @PostMapping("/upload")
    public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {
        var fileName = service.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
        return new UploadFileResponseDTO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

}
