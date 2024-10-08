package com.project.house.rental.controller;

import com.project.house.rental.dto.AdvertisementDto;
import com.project.house.rental.service.AdvertisementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @PostMapping
    public ResponseEntity<AdvertisementDto> createAdvertisement(
            @Valid @ModelAttribute AdvertisementDto advertisementDto,
            @RequestParam(required = false) MultipartFile image) throws IOException {
        AdvertisementDto createdAdvertisement = advertisementService.createAdvertisement(advertisementDto, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdvertisement);
    }


    @PutMapping("/{id}")
    public ResponseEntity<AdvertisementDto> updateAdvertisement(
            @PathVariable Long id,
            @Valid @ModelAttribute AdvertisementDto advertisementDto,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        AdvertisementDto updatedAdvertisement = advertisementService.updateAdvertisement(id, advertisementDto, image);
        return new ResponseEntity<>(updatedAdvertisement, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementDto> getAdvertisementById(@PathVariable Long id) {
        AdvertisementDto advertisementDto = advertisementService.getAdvertisementById(id);
        return new ResponseEntity<>(advertisementDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable Long id) {
        advertisementService.deleteAdvertisement(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<AdvertisementDto>> getAllAdvertisements() {
        List<AdvertisementDto> advertisements = advertisementService.getAllAdvertisements();
        return new ResponseEntity<>(advertisements, HttpStatus.OK);
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<AdvertisementDto> updateIsActived(@PathVariable Long id) {
        AdvertisementDto updatedAdvertisement = advertisementService.updateIsActived(id);
        return new ResponseEntity<>(updatedAdvertisement, HttpStatus.OK);
    }
}
