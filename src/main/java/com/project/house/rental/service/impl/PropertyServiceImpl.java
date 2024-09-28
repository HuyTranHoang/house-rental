package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.dto.params.PropertyParams;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.PropertyImage;
import com.project.house.rental.entity.Property_;
import com.project.house.rental.entity.UserMembership;
import com.project.house.rental.mapper.PropertyMapper;
import com.project.house.rental.repository.PropertyImageRepository;
import com.project.house.rental.repository.PropertyRepository;
import com.project.house.rental.repository.UserMembershipRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.CloudinaryService;
import com.project.house.rental.service.PropertyService;
import com.project.house.rental.service.email.EmailSenderService;
import com.project.house.rental.specification.PropertySpecification;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;


@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final CloudinaryService cloudinaryService;
    private final HibernateFilterHelper hibernateFilterHelper;
    private final PropertyMapper propertyMapper;
    private final PropertyImageRepository propertyImageRepository;
    private final JWTTokenProvider jwtTokenProvider;
    private final EmailSenderService emailSenderService;
    private final UserMembershipRepository userMembershipRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository, CloudinaryService cloudinaryService, HibernateFilterHelper hibernateFilterHelper, PropertyMapper propertyMapper, PropertyImageRepository propertyImageRepository, JWTTokenProvider jwtTokenProvider, EmailSenderService emailSenderService, UserMembershipRepository userMembershipRepository) {
        this.propertyRepository = propertyRepository;
        this.cloudinaryService = cloudinaryService;
        this.hibernateFilterHelper = hibernateFilterHelper;
        this.propertyMapper = propertyMapper;
        this.propertyImageRepository = propertyImageRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailSenderService = emailSenderService;
        this.userMembershipRepository = userMembershipRepository;
    }


    @Override
    public PropertyDto getPropertyById(long id) {
        Property property = propertyRepository.findByIdWithFilter(id);

        if (property == null) {
            throw new NoResultException("Không tìm thấy tin đăng với id: " + id);
        }

        return propertyMapper.toDto(property);
    }

    @Override
    public PropertyDto createProperty(PropertyDto propertyDto, MultipartFile[] images) throws IOException {
        Property property = propertyMapper.toEntity(propertyDto);
        List<PropertyImage> propertyImages = new ArrayList<>();

        if (images != null && images.length > 0) {
            Map<String, Object> cloudinaryResponse = cloudinaryService.uploadImages(images);

            for (Map.Entry<String, Object> entry : cloudinaryResponse.entrySet()) {
                String publicId = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof Map<?, ?> imageDetails) {
                    String blurhash = (String) imageDetails.get("blurhash");
                    String imageName = (String) imageDetails.get("imageName");

                    PropertyImage propertyImage = PropertyImage.builder()
                            .imageUrl(cloudinaryService.getOptimizedImage(publicId))
                            .publicId(publicId)
                            .blurhash(blurhash)
                            .property(property)
                            .build();

                    if (propertyDto.getThumbnailOriginalName().equals(imageName)) {
                        property.setThumbnailUrl(propertyImage.getImageUrl());
                        property.setThumbnailBlurhash(propertyImage.getBlurhash());
                    }

                    propertyImages.add(propertyImage);
                } else {
                    throw new IllegalStateException("Unexpected value type in cloudinary response");
                }
            }
        }

        property.setPropertyImages(propertyImages);

        // Set default refreshDay and priorityExpiration '1970-01-01 00:00:00'
        LocalDateTime localDateTime = LocalDateTime.of(1970, 1, 1, 0, 0);
        Date date = Date.from(localDateTime.toInstant(ZoneOffset.UTC));
        property.setRefreshDay(date);
        property.setPriorityExpiration(date);

        propertyRepository.save(property);
        propertyImageRepository.saveAll(propertyImages);
        return propertyMapper.toDto(property);
    }

    @Override
    public PropertyDto updateProperty(long id, PropertyDto propertyDto, MultipartFile[] images) throws IOException {
        Property property = propertyRepository.findByIdWithFilter(id);
        if (property == null) {
            throw new NoResultException("Không tìm thấy bất động sản với id: " + id);
        }

        // Update images if provided
        if (images != null && images.length > 0) {
            Map<String, Object> cloudinaryResponse = cloudinaryService.uploadImages(images);

            for (Map.Entry<String, Object> entry : cloudinaryResponse.entrySet()) {
                String publicId = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof Map<?, ?> imageDetails) {
                    String blurhash = (String) imageDetails.get("blurhash");

                    PropertyImage propertyImage = PropertyImage.builder()
                            .imageUrl(cloudinaryService.getOptimizedImage(publicId))
                            .publicId(publicId)
                            .blurhash(blurhash)
                            .property(property)
                            .build();
                    propertyImageRepository.save(propertyImage);
                    property.getPropertyImages().add(propertyImage);
                } else {
                    throw new IllegalStateException("Unexpected value type in cloudinary response");
                }
            }
        }

        propertyMapper.updateEntityFromDto(propertyDto, property);

        // Handle deleted images
        property.getPropertyImages().forEach(propertyImage -> {
            if (propertyImage.isDeleted()) {
                propertyImageRepository.delete(propertyImage);
            }
        });

        // Update thumbnail if specified
//        String thumbnailOriginalName = propertyDto.getThumbnailOriginalName();
//        if (thumbnailOriginalName != null) {
//            property.getPropertyImages().stream()
//                    .filter(image -> thumbnailOriginalName.equals(image.getPublicId()))
//                    .findFirst()
//                    .ifPresent(thumbnailImage -> {
//                        property.setThumbnailUrl(thumbnailImage.getImageUrl());
//                        property.setThumbnailBlurhash(thumbnailImage.getBlurhash());
//                    });
//        }

        propertyRepository.save(property);
        return propertyMapper.toDto(property);
    }

    @Override
    public void deletePropertyById(long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy tin đăng với id: " + id));

        propertyRepository.deleteById(property.getId());
    }


    public Map<String, Object> getAllPropertiesWithParams(PropertyParams propertyParams) {
        Specification<Property> spec = PropertySpecification.searchByCityDistrictLocation(propertyParams.getSearch())
                .and(PropertySpecification.filterByCityId(propertyParams.getCityId()))
                .and(PropertySpecification.filterByDistrictId(propertyParams.getDistrictId()))
                .and(PropertySpecification.filterByRoomTypeId(propertyParams.getRoomTypeId()))
                .and(PropertySpecification.filterByPrice(propertyParams.getMinPrice(), propertyParams.getMaxPrice()))
                .and(PropertySpecification.filterByArea(propertyParams.getMinArea(), propertyParams.getMaxArea()))
                .and(PropertySpecification.filterByCreatedDate(propertyParams.getNumOfDays()))
                .and(PropertySpecification.filterByStatus(propertyParams.getStatus()))
                .and(PropertySpecification.filterByUserId(propertyParams.getUserId()))
                .and(PropertySpecification.filterByBlocked(propertyParams.getIsBlocked()))
                .and(PropertySpecification.filterByHidden(propertyParams.getIsHidden()));


        Sort sort = switch (propertyParams.getSortBy()) {
            case "priceDesc" -> Sort.by(Property_.PRICE).descending();
            case "priceAsc" -> Sort.by(Property_.PRICE);
            case "areaDesc" -> Sort.by(Property_.AREA).descending();
            case "areaAsc" -> Sort.by(Property_.AREA);
            case "createdAtAsc" -> Sort.by(Property_.CREATED_AT);
            case "createdAtDesc" -> Sort.by(Property_.CREATED_AT).descending();
            case "refreshDayAsc" -> Sort.by(Property_.REFRESH_DAY);
            default -> Sort.by(Property_.REFRESH_DAY).descending();
        };

        if (propertyParams.getPageNumber() < 0) {
            propertyParams.setPageNumber(0);
        }

        if (propertyParams.getPageSize() <= 0) {
            propertyParams.setPageSize(10);
        }

        Pageable pageable = PageRequest.of(
                propertyParams.getPageNumber(),
                propertyParams.getPageSize(),
                sort
        );

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_PROPERTY_FILTER);

        Page<Property> propertyPage = propertyRepository.findAll(spec, pageable);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_PROPERTY_FILTER);

        PageInfo pageInfo = new PageInfo(propertyPage);

        List<PropertyDto> propertyDtoList = propertyPage.stream()
                .map(propertyMapper::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", propertyDtoList
        );
    }

    @Override
    public PropertyDto blockProperty(long id, String status) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy bài đăng !"));

        if (status.equals("unblock")) {
            property.setBlocked(false);
            propertyRepository.save(property);
            //TODO: Bat len khi demo -- Chưa có mail unblock
//            emailSenderService.sendUnblockHTMLMail(property.getUser().getEmail(), property.getUser().getUsername(), property.getTitle());
            return propertyMapper.toDto(property);
        }

        property.setBlocked(true);
        propertyRepository.save(property);
        //TODO: Bat len khi demo
//        emailSenderService.sendBlockHTMLMail(property.getUser().getEmail(), property.getUser().getUsername(), property.getTitle());
        return propertyMapper.toDto(property);
    }

    @Override
    public PropertyDto updatePropertyStatus(long id, String status) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy bài đăng !"));

        if (!isValidPropertyStatus(status)) {
            throw new IllegalArgumentException("Trạng thái [" + status + "] không hợp lệ!");
        }

        property.setStatus(Property.PropertyStatus.valueOf(status));
        propertyRepository.save(property);
        return propertyMapper.toDto(property);
    }

    @Override
    public PropertyDto hideProperty(long id, HttpServletRequest request) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy bài đăng !"));

        String username = jwtTokenProvider.getUsernameFromToken(request);

        if (!property.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Bạn không có quyền thực hiện hành động này !");
        }

        property.setHidden(!property.isHidden());
        propertyRepository.save(property);
        return propertyMapper.toDto(property);
    }

    @Override
    public PropertyDto refreshProperty(long id, HttpServletRequest request) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy bài đăng !"));

        String username = jwtTokenProvider.getUsernameFromToken(request);

        UserMembership userMembership = getValidationUserMembership(property, username);
        userMembershipRepository.save(userMembership);

        property.setRefreshDay(new Date());
        propertyRepository.save(property);
        return propertyMapper.toDto(property);
    }

    private static UserMembership getValidationUserMembership(Property property, String username) {
        if (!property.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Bạn không có quyền thực hiện hành động này !");
        }

        int refreshLimit = property.getUser().getUserMembership().getTotalPriorityLimit();
        int refreshUsed = property.getUser().getUserMembership().getRefreshesPostsUsed();

        if (refreshUsed >= refreshLimit) {
            throw new IllegalArgumentException("Bạn đã sử dụng hết số lần refresh tin đăng !");
        }


        UserMembership userMembership = property.getUser().getUserMembership();
        userMembership.setRefreshesPostsUsed(refreshUsed + 1);
        return userMembership;
    }


    public static boolean isValidPropertyStatus(String status) {
        try {
            Property.PropertyStatus.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")  // Chạy vào lúc 0 giờ mỗi ngày
    public void updateExpiredPriorities() {
        Date now = new Date(System.currentTimeMillis());
        List<Property> expiredProperties = propertyRepository.findByPriorityExpirationBeforeAndPriorityTrue(now);
        expiredProperties.forEach(property -> {
            property.setPriority(false);
            propertyRepository.save(property);
        });
    }

    @Scheduled(cron = "0 0 0 * * ?") // Tự cập nhật lại refreshDay sau 2 ngày
    public void resetRefreshDayAfterTwoDays() {
        Date twoDaysAgo = new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000);

        List<Property> propertiesToUpdate = propertyRepository.findByRefreshDayBefore(twoDaysAgo);
        propertiesToUpdate.forEach(property -> property.setRefreshDay(property.getCreatedAt()));

        propertyRepository.saveAll(propertiesToUpdate);
    }


}
