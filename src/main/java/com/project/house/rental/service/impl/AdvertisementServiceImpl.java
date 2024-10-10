package com.project.house.rental.service.impl;

import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.AdvertisementDto;
import com.project.house.rental.entity.Advertisement;
import com.project.house.rental.entity.Advertisement_;
import com.project.house.rental.mapper.AdvertisementMapper;
import com.project.house.rental.repository.AdvertisementRepository;
import com.project.house.rental.service.AdvertisementService;
import com.project.house.rental.service.CloudinaryService;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final CloudinaryService cloudinaryService;
    private final HibernateFilterHelper hibernateFilterHelper;
    private final AdvertisementMapper advertisementMapper;

    public AdvertisementServiceImpl(AdvertisementRepository advertisementRepository, CloudinaryService cloudinaryService, HibernateFilterHelper hibernateFilterHelper, AdvertisementMapper advertisementMapper) {
        this.advertisementRepository = advertisementRepository;
        this.cloudinaryService = cloudinaryService;
        this.hibernateFilterHelper = hibernateFilterHelper;
        this.advertisementMapper = advertisementMapper;
    }

    @Override
    public AdvertisementDto createAdvertisement(AdvertisementDto advertisementDto, MultipartFile image) throws IOException {
        Advertisement advertisement = new Advertisement();
        advertisement.setName(advertisementDto.getName());
        advertisement.setDescription(advertisementDto.getDescription());

        if (image != null && !image.isEmpty()) {
            String publicId = cloudinaryService.upload(image);
            advertisement.setImageUrl(cloudinaryService.getOptimizedImage(publicId));
        }

        advertisementRepository.save(advertisement);
        return advertisementMapper.toDto(advertisement);
    }


    @Override
    public AdvertisementDto updateAdvertisement(Long id, AdvertisementDto advertisementDto, MultipartFile image) throws IOException {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Advertisement not found"));

        advertisement.setName(advertisementDto.getName());
        advertisement.setDescription(advertisementDto.getDescription());

        if (image != null) {
            String publicId = cloudinaryService.upload(image);
            advertisement.setImageUrl(cloudinaryService.getOptimizedImage(publicId));
        }

        advertisementRepository.save(advertisement);
        return advertisementMapper.toDto(advertisement);
    }

    @Override
    public AdvertisementDto getAdvertisementById(Long id) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Advertisement not found"));
        return advertisementMapper.toDto(advertisement);
    }

    @Override
    public void deleteAdvertisement(Long id) {
        advertisementRepository.deleteById(id);
    }

    @Override
    public List<AdvertisementDto> getAllAdvertisements(String isActived) {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_ADVERTISEMENT_FILTER);

        Specification<Advertisement> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();

        if (StringUtils.hasLength(isActived) && isActived.equals("true")) {
            spec = (root, query, criteriaBuilder) ->
                    criteriaBuilder.and(criteriaBuilder.isTrue(root.get(Advertisement_.IS_ACTIVED)));
        }

        List<Advertisement> advertisementList = advertisementRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"));

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_ADVERTISEMENT_FILTER);

        return advertisementList.stream()
                .map(AdvertisementMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public AdvertisementDto updateIsActived(Long id) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Advertisement not found"));

        advertisement.setActived(!advertisement.isActived());

        advertisementRepository.save(advertisement);

        return advertisementMapper.toDto(advertisement);
    }


}
