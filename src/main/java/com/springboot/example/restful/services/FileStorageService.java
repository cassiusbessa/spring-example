package com.springboot.example.restful.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.example.restful.config.FileStorageConfig;
import com.springboot.example.restful.exceptions.FileStorageException;
import com.springboot.example.restful.exceptions.ResourceNotFoundException;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @SuppressWarnings("null")
    public String storeFile(MultipartFile file) {
        String fileName =  StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }
        if (fileName.isEmpty() || fileName == null || fileName.isBlank()) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence ");
        }   
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            // Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            file.transferTo(targetLocation.toFile());
            return fileName;
        } catch (Exception ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                System.out.println("fileName: " + fileName);
                throw new ResourceNotFoundException("File not found " + fileName);
            }
        } catch (Exception ex) {
            System.out.println("fileName: " + fileName);
            throw new ResourceNotFoundException("File not found " + fileName, ex);
        }
    }



}
