package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.PropertyDto;
import com.project.house.rental.dto.params.PropertyParams;
import com.project.house.rental.entity.*;
import com.project.house.rental.mapper.PropertyMapper;
import com.project.house.rental.repository.NotificationRespository;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


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
    private final NotificationRespository notificationRespository;

    public PropertyServiceImpl(PropertyRepository propertyRepository, CloudinaryService cloudinaryService, HibernateFilterHelper hibernateFilterHelper, PropertyMapper propertyMapper, PropertyImageRepository propertyImageRepository, JWTTokenProvider jwtTokenProvider, EmailSenderService emailSenderService, UserMembershipRepository userMembershipRepository, NotificationRespository notificationRespository) {
        this.propertyRepository = propertyRepository;
        this.cloudinaryService = cloudinaryService;
        this.hibernateFilterHelper = hibernateFilterHelper;
        this.propertyMapper = propertyMapper;
        this.propertyImageRepository = propertyImageRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailSenderService = emailSenderService;
        this.userMembershipRepository = userMembershipRepository;
        this.notificationRespository = notificationRespository;
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
    public PropertyDto createProperty(PropertyDto propertyDto, MultipartFile[] images, MultipartFile thumbnailImage) throws IOException {
        Property property = propertyMapper.toEntity(propertyDto);
        List<PropertyImage> propertyImages = new ArrayList<>();

        if (images != null && images.length > 0) {
            CompletableFuture<List<String>> cloudinaryResponseFuture = cloudinaryService.uploadImages(images);

            List<String> cloudinaryResponse;
            try {
                cloudinaryResponse = cloudinaryResponseFuture.get(); // Wait for the upload to complete
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Failed to upload images", e);
            }

            for (String publicId : cloudinaryResponse) {
                PropertyImage propertyImage = PropertyImage.builder()
                        .imageUrl(cloudinaryService.getOptimizedImage(publicId))
                        .publicId(publicId)
                        .property(property)
                        .build();
                propertyImages.add(propertyImage);
            }
        }

        if (thumbnailImage != null) {
            String publicId = cloudinaryService.upload(thumbnailImage);

            PropertyImage thumbnailPropertyImage = PropertyImage.builder()
                    .imageUrl(cloudinaryService.getOptimizedImage(publicId))
                    .publicId(publicId)
                    .property(property)
                    .build();

            property.setThumbnailUrl(thumbnailPropertyImage.getImageUrl());
            propertyImages.add(thumbnailPropertyImage);
        }

        property.setPropertyImages(propertyImages);

        // Set default priorityExpiration '1970-01-01 00:00:00'
        LocalDateTime localDateTime = LocalDateTime.of(1970, 1, 1, 0, 0);
        Date date = Date.from(localDateTime.toInstant(ZoneOffset.UTC));
        property.setPriorityExpiration(date);

        property.setRefreshedAt(new Date());

        propertyRepository.save(property);
        propertyImageRepository.saveAll(propertyImages);
        return propertyMapper.toDto(property);
    }

    @Override
    public PropertyDto updateProperty(long id, PropertyDto propertyDto, MultipartFile[] images) {
        Property property = propertyRepository.findByIdWithFilter(id);
        if (property == null) {
            throw new NoResultException("Không tìm thấy bất động sản với id: " + id);
        }

        // Update images if provided
//        if (images != null && images.length > 0) {
//            Map<String, Object> cloudinaryResponse = cloudinaryService.uploadImages(images);
//
//            for (Map.Entry<String, Object> entry : cloudinaryResponse.entrySet()) {
//                String publicId = entry.getKey();
//                Object value = entry.getValue();
//
//                if (value instanceof Map<?, ?> imageDetails) {
//                    String blurhash = (String) imageDetails.get("blurhash");
//
//                    PropertyImage propertyImage = PropertyImage.builder()
//                            .imageUrl(cloudinaryService.getOptimizedImage(publicId))
//                            .publicId(publicId)
//                            .blurhash(blurhash)
//                            .property(property)
//                            .build();
//                    propertyImageRepository.save(propertyImage);
//                    property.getPropertyImages().add(propertyImage);
//                } else {
//                    throw new IllegalStateException("Unexpected value type in cloudinary response");
//                }
//            }
//        }

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
    public PropertyDto selfUpdateProperty(long id, PropertyDto propertyDto, MultipartFile[] images, String deleteImages, HttpServletRequest request) throws IOException {
        Property property = propertyRepository.findByIdWithFilter(id);
        if (property == null) {
            throw new NoResultException("Không tìm thấy bất động sản với id: " + id);
        }

        String username = jwtTokenProvider.getUsernameFromToken(request);
        if (!property.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Bạn không có quyền thực hiện hành động này !");
        }

        if (images != null && images.length > 0) {
            List<PropertyImage> propertyImages = new ArrayList<>();
            CompletableFuture<List<String>> cloudinaryResponseFuture = cloudinaryService.uploadImages(images);

            List<String> cloudinaryResponse;
            try {
                cloudinaryResponse = cloudinaryResponseFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Failed to upload images", e);
            }

            for (String publicId : cloudinaryResponse) {
                PropertyImage propertyImage = PropertyImage.builder()
                        .imageUrl(cloudinaryService.getOptimizedImage(publicId))
                        .publicId(publicId)
                        .property(property)
                        .build();
                propertyImages.add(propertyImage);
            }

            property.getPropertyImages().addAll(propertyImages);
            propertyImageRepository.saveAll(propertyImages);
        }

        if (StringUtils.hasLength(deleteImages)) {
            List<String> deletedImageIds = Arrays.asList(deleteImages.split("\\|"));
            property.getPropertyImages().forEach(propertyImage -> {
                if (deletedImageIds.contains(propertyImage.getImageUrl())) {
                    propertyImage.setDeleted(true);
                    try {
                        if (propertyImage.getPublicId() != null) {
                            cloudinaryService.delete(propertyImage.getPublicId());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        propertyDto.setUserId(property.getUser().getId());
        propertyMapper.updateEntityFromDto(propertyDto, property);

        propertyRepository.save(property);

        return propertyMapper.toDto(property);
    }

    @Override
    public void deletePropertyById(long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy tin đăng với id: " + id));

        propertyRepository.deleteById(property.getId());
    }

    @Override
    public void selfDeletePropertyById(long id, HttpServletRequest request) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy tin đăng với id: " + id));

        String username = jwtTokenProvider.getUsernameFromToken(request);

        if (!property.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Bạn không có quyền thực hiện hành động này !");
        }

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
            case "refreshDayAsc" -> Sort.by(Property_.REFRESHED_AT);
            default -> Sort.by(Property_.REFRESHED_AT).descending();
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

//        Page<Property> normalPropertiesPage = propertyRepository.findAll(spec.and(PropertySpecification.filterByPriority(false)), pageable);
//
//        int priorityPageSize = 3; // Number of priority properties per page
//        if (normalPropertiesPage.getTotalElements() < propertyParams.getPageSize()) {
//            priorityPageSize = propertyParams.getPageSize();
//        }
//        // Fetch priority properties with the calculated offset
//
//        int priorityPageNumber = propertyParams.getPageNumber(); // Current page number
//
//        Pageable priorityPageable = PageRequest.of(
//                priorityPageNumber,
//                priorityPageSize,
//                Sort.by(Property_.REFRESHED_AT).descending()
//        );
//        Page<Property> priorityPropertiesPage = propertyRepository.findAll(PropertySpecification.filterByPriority(true), priorityPageable);
//        List<Property> priorityProperties = priorityPropertiesPage.getContent();
//
//        // Fetch normal properties with pagination
//
//
        Page<Property> propertyPage = propertyRepository.findAll(spec, pageable);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_PROPERTY_FILTER);

        List<PropertyDto> propertyDtoList = propertyPage.stream()
                .map(propertyMapper::toDto)
                .toList();

        PageInfo pageInfo = new PageInfo(propertyPage);
//
//        List<PropertyDto> propertyDtoList = new ArrayList<>();
//        propertyDtoList.addAll(priorityProperties.stream().map(propertyMapper::toDto).toList());
//        propertyDtoList.addAll(normalPropertiesPage.stream().map(propertyMapper::toDto).toList());


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
            emailSenderService.sendUnblockHTMLMail(property.getUser().getEmail(), property.getUser().getUsername(), property.getTitle());
            return propertyMapper.toDto(property);
        }

        property.setBlocked(true);
        propertyRepository.save(property);
        emailSenderService.sendBlockHTMLMail(property.getUser().getEmail(), property.getUser().getUsername(), property.getTitle());
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

        Notification notification = Notification.builder()
                .user(property.getUser())
                .sender(null)
                .property(property)
                .comment(null)
                .isSeen(false)
                .build();

        if (status.equals("APPROVED")) {
            notification.setType(Notification.NotificationType.APPROVED);
        } else if (status.equals("REJECTED")) {
            notification.setType(Notification.NotificationType.REJECTED);
        }
        notificationRespository.save(notification);
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

        UserMembership userMembership = getValidationUserMembership(property, username, true);
        userMembershipRepository.save(userMembership);

        property.setRefreshedAt(new Date());
        propertyRepository.save(property);
        return propertyMapper.toDto(property);
    }

    @Override
    public PropertyDto prioritizeProperty(long id, HttpServletRequest request) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy bài đăng !"));

        String username = jwtTokenProvider.getUsernameFromToken(request);

        UserMembership userMembership = getValidationUserMembership(property, username, false);
        userMembershipRepository.save(userMembership);

        property.setPriority(true);
        property.setPriorityExpiration(new Date());
        propertyRepository.save(property);
        return propertyMapper.toDto(property);
    }

    @Override
    public List<PropertyDto> getPriorityProperties() {
        List<Property> priorityProperties = propertyRepository.findByIsPriorityTrue();

        return priorityProperties.stream()
                .sorted(Comparator.comparing(Property::getRefreshedAt).reversed())
                .map(propertyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PropertyDto> getRelatedProperties(long propertyId) {
        Property property = propertyRepository.findByIdWithFilter(propertyId);
        if (property == null) {
            throw new NoResultException("Không tìm thấy tin đăng với id: " + propertyId);
        }

        long cityId = property.getCity().getId();
        long districtId = property.getDistrict().getId();
        long roomTypeId = property.getRoomType().getId();

        List<Property> relatedProperties = propertyRepository.findRelatedProperties(
                cityId,
                districtId,
                roomTypeId,
                propertyId
        );

        if (relatedProperties.size() < 4) {
            relatedProperties.addAll(propertyRepository.findRelatedPropertiesByCityAndRoomType(
                    cityId,
                    roomTypeId,
                    propertyId
            ));
        }

        if (relatedProperties.size() < 4) {
            relatedProperties.addAll(propertyRepository.findRelatedPropertiesByCity(
                    cityId,
                    propertyId
            ));
        }

        if (relatedProperties.size() < 4) {
            relatedProperties.addAll(propertyRepository.findAllPropertiesExcept(propertyId));
        }

        // Remove duplicates
        relatedProperties = relatedProperties.stream().distinct().collect(Collectors.toList());

        // Shuffle and limit to 4
        Collections.shuffle(relatedProperties);
        relatedProperties = relatedProperties.stream().limit(4).collect(Collectors.toList());

        return relatedProperties.stream()
                .map(propertyMapper::toDto)
                .collect(Collectors.toList());
    }

    private static UserMembership getValidationUserMembership(Property property, String username, boolean isRefresh) {
        if (!property.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Bạn không có quyền thực hiện hành động này !");
        }

        UserMembership userMembership = property.getUser().getUserMembership();

        if (isRefresh) {
            int refreshLimit = property.getUser().getUserMembership().getTotalPriorityLimit();
            int refreshUsed = property.getUser().getUserMembership().getRefreshesPostsUsed();

            if (refreshUsed >= refreshLimit) {
                throw new IllegalArgumentException("Bạn đã sử dụng hết số lần làm mới tin đăng !");
            }

            userMembership.setRefreshesPostsUsed(refreshUsed + 1);
        } else {
            int priorityLimit = property.getUser().getUserMembership().getTotalPriorityLimit();
            int priorityUsed = property.getUser().getUserMembership().getPriorityPostsUsed();

            if (priorityUsed >= priorityLimit) {
                throw new IllegalArgumentException("Bạn đã sử dụng hết số lần ưu tiên tin đăng !");
            }

            userMembership.setPriorityPostsUsed(priorityUsed + 1);
        }

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

        List<Property> propertiesToUpdate = propertyRepository.findByRefreshedAtBefore(twoDaysAgo);
        propertiesToUpdate.forEach(property -> property.setRefreshedAt(property.getCreatedAt()));

        propertyRepository.saveAll(propertiesToUpdate);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Tự cập nhật lại refreshDay sau 2 ngày
    public void resetPriorityDayAfterTwoDays() {
        Date twoDaysAgo = new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000);

        List<Property> propertiesToUpdate = propertyRepository.findByPriorityExpirationBefore(twoDaysAgo);
        propertiesToUpdate.forEach(property -> property.setPriority(false));

        propertyRepository.saveAll(propertiesToUpdate);
    }


}
