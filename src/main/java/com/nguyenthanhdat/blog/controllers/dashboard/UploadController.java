package com.nguyenthanhdat.blog.controllers.dashboard;

import com.nguyenthanhdat.blog.exceptions.FileUploadException;
import com.nguyenthanhdat.blog.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/v1/uploads")
@RequiredArgsConstructor
public class UploadController {
    private final FileStorageService fileStorageService;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new FileUploadException("Please select a file to upload.");
            }

            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("image/jpeg") &&
                    !contentType.equals("image/png") &&
                    !contentType.equals("image/jpg"))) {
                throw new FileUploadException("Only JPG, JPEG, PNG files are allowed.");
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new FileUploadException("File size exceeds the maximum limit of 5MB.");
            }

            String imageUrl = fileStorageService.uploadFile(file);
            return ResponseEntity.ok(Map.of("url", imageUrl));
        } catch (FileUploadException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to upload file: " + e.getMessage()));
        }
    }
}


