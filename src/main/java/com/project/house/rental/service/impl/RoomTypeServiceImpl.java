package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.dto.RoomTypeDto;
import com.project.house.rental.dto.params.RoomTypeParams;
import com.project.house.rental.entity.RoomType;
import com.project.house.rental.entity.RoomType_;
import com.project.house.rental.repository.GenericRepository;
import com.project.house.rental.repository.RoomTypeRepository;
import com.project.house.rental.service.RoomTypeService;
import com.project.house.rental.specification.RoomTypeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class RoomTypeServiceImpl extends GenericServiceImpl<RoomType, RoomTypeDto> implements RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeServiceImpl(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    public Map<String, Object> getAllRoomTypesWithParams(RoomTypeParams roomTypeParams) {
        Specification<RoomType> specification = RoomTypeSpecification.searchByName(roomTypeParams.getName());

        if (!StringUtils.hasLength(roomTypeParams.getSortBy())) {
            roomTypeParams.setSortBy("createdAtDesc");
        }

        Sort sort = switch (roomTypeParams.getSortBy()) {
            case "nameAsc" -> Sort.by(RoomType_.NAME);
            case "nameDesc" -> Sort.by(RoomType_.NAME).descending();
            case "createdAtAsc" -> Sort.by(RoomType_.CREATED_AT);
            default -> Sort.by(RoomType_.CREATED_AT).descending();
        };

        if (roomTypeParams.getPageNumber() < 0) {
            roomTypeParams.setPageNumber(0);
        }

        if (roomTypeParams.getPageSize() <= 0) {
            roomTypeParams.setPageSize(10);
        }

        Pageable pageable = PageRequest.of(
                roomTypeParams.getPageNumber(),
                roomTypeParams.getPageSize(),
                sort
        );

        Page<RoomType> roomTypePage = roomTypeRepository.findAll(specification, pageable);

        PageInfo pageInfo = new PageInfo(
          roomTypePage.getNumber(),
          roomTypePage.getTotalElements(),
          roomTypePage.getTotalPages(),
          roomTypePage.getSize()
        );

        List<RoomTypeDto> roomTypeDtoList = roomTypePage.stream()
                .map(this::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", roomTypeDtoList
        );
    }

    @Override
    protected GenericRepository<RoomType> getRepository() {
        return roomTypeRepository;
    }

    @Override
    public RoomTypeDto toDto(RoomType roomType) {
        return RoomTypeDto.builder()
                .id(roomType.getId())
                .name(roomType.getName())
                .build();
    }

    @Override
    public RoomType toEntity(RoomTypeDto roomTypeDto) {
        return RoomType.builder()
                .name(roomTypeDto.getName())
                .build();
    }

    @Override
    public void updateEntityFromDto(RoomType roomType, RoomTypeDto roomTypeDto) {
        roomType.setName(roomTypeDto.getName());
    }

    @Override
    public RoomTypeDto create(RoomTypeDto roomTypeDto) {
        RoomType existingRoomType = roomTypeRepository.findByNameIgnoreCase(roomTypeDto.getName());

        if (existingRoomType != null) {
            throw new IllegalArgumentException("Room type with name " + roomTypeDto.getName() + " already exists");
        }

        return super.create(roomTypeDto);
    }

    @Override
    public RoomTypeDto update(long id, RoomTypeDto roomTypeDto) {
        RoomType existingRoomType = roomTypeRepository.findByNameIgnoreCase(roomTypeDto.getName());

        if (existingRoomType != null) {
            throw new IllegalArgumentException("Room type with name " + roomTypeDto.getName() + " already exists");
        }

        return super.update(id, roomTypeDto);
    }

}
