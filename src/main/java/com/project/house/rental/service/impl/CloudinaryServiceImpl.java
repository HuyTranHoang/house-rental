package com.project.house.rental.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.project.house.rental.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(
            @Value("${cloudinary.cloudName}") String cloudName,
            @Value("${cloudinary.apiKey}") String apiKey,
            @Value("${cloudinary.apiSecret}") String apiSecret
    ) {
        Map<String, String> config = Map.of(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        );

        this.cloudinary = new Cloudinary(config);
    }

    @Override
    public Map upload(MultipartFile file, String publicId) throws IOException {
        File fileToUpload = convertMultiPartToFile(file);
        Map<String, Object> uploadParams = Map.of("public_id", publicId,
                "folder", "house-rental");
        Map result = cloudinary.uploader().upload(fileToUpload, uploadParams);
        if (!Files.deleteIfExists(fileToUpload.toPath())) {
            throw new IOException("Failed to delete file");
        }

        return result;
    }

    @Override
    public CompletableFuture<Map<String, String>> uploadImages(MultipartFile[] files) throws IOException {
        Map<String, String> result = new HashMap<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (MultipartFile file : files) {
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                try {
                    File fileToUpload = convertMultiPartToFile(file);
                    Map<String, String> uploadParams = Map.of("folder", "house-rental");
                    Map uploadResult = cloudinary.uploader().upload(fileToUpload, uploadParams);
                    if (!Files.deleteIfExists(fileToUpload.toPath())) {
                        throw new IOException("Failed to delete file");
                    }

                    String publicId = (String) uploadResult.get("public_id");
                    String imageName = file.getOriginalFilename();

                    synchronized (result) {
                        result.put(publicId, imageName);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });

            futures.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            allOf.get(); // Wait for all futures to complete
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return CompletableFuture.completedFuture(result);
    }

    @Override
    public Map delete(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, Map.of());
    }

    @Override
    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();

        return convertedFile;
    }

    @Override
    public String getOptimizedImage(String publicId) {
        return cloudinary.url()
                .transformation(new Transformation().quality("auto").fetchFormat("auto"))
                .secure(true)
                .generate(publicId);
    }
}
