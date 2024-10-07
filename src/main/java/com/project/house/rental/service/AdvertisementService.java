package com.project.house.rental.service;

import com.project.house.rental.dto.AdvertisementDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdvertisementService {

    AdvertisementDto createAdvertisement(AdvertisementDto advertisementDto, MultipartFile image) throws IOException;

    AdvertisementDto updateAdvertisement(Long id, AdvertisementDto advertisementDto, MultipartFile image) throws IOException;

    AdvertisementDto getAdvertisementById(Long id);

    void deleteAdvertisement(Long id);

    List<AdvertisementDto> getAllAdvertisements();
}
