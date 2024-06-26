package com.springboot.example.restful.controller;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.springboot.example.restful.dto.v1.UploadFileResponseDTO;
import com.springboot.example.restful.services.FileStorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "File endpoints", description = "File API's for uploading and downloading files")
@RestController
@RequestMapping("/file")
public class FileController {

    private Logger logger = Logger.getLogger(FileController.class.getName());

    @Autowired  
    private FileStorageService service;

    @Operation(summary = "Upload a file")
    @Description("This endpoint allows you to upload a file")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = UploadFileResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @PostMapping("/upload")
    public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {
        logger.info("Uploading file: " + file.getOriginalFilename());
        var fileName = service.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/file/download/")
                .path(fileName)
                .toUriString();
        return new UploadFileResponseDTO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }


    @Operation(summary = "Download a file")
    @Description("This endpoint allows you to download a file")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
   @GetMapping("/download/{filename:.+}")
	public ResponseEntity<Resource> downloadFile(
		@PathVariable String filename, HttpServletRequest request) {
        
        logger.info("Downloading file: " + filename);

        Resource resource = service.loadFileAsResource(filename);
        String contentType = "";

        try {
            logger.info("Downloading file: " + filename);
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (Exception ex) {
            logger.info("Could not determine file type.");
        }
        if (contentType == null || contentType.isEmpty() || contentType.isBlank()){
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
