package com.project.house.rental.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CloudinaryService {
    Map upload(MultipartFile file, String publicId) throws IOException;

    String upload(MultipartFile file) throws IOException;

    CompletableFuture<List<String>> uploadImages(MultipartFile[] files) throws IOException;

    Map delete(String publicId) throws IOException;

    File convertMultiPartToFile(MultipartFile file) throws IOException;

    String getOptimizedImage(String publicId);
}
