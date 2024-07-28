package com.project.house.rental.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {
    Map upload(MultipartFile file, String publicId) throws IOException;

    Map delete(String publicId) throws IOException;

    File convertMultiPartToFile(MultipartFile file) throws IOException;
}
