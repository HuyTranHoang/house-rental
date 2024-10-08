package com.project.house.rental.controller;

import com.project.house.rental.service.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary")
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    public CloudinaryController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map> upload(@RequestParam MultipartFile file, @RequestParam String publicId) {
        try {
            Map result = cloudinaryService.upload(file, "house-rental/" + publicId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/uploadImages")
    public ResponseEntity<Map> uploadImages(@RequestParam("files") MultipartFile[] files) {
//        try {
//            Map result = cloudinaryService.uploadImages(files);
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Not implemented"));
//        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map> delete(@RequestParam String publicId) {
        try {
            Map result = cloudinaryService.delete(publicId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
