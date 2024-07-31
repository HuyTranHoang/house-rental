package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.RoomTypeDto;
import com.project.house.rental.dto.params.RoomTypeParams;
import com.project.house.rental.entity.RoomType;
import com.project.house.rental.entity.RoomType_;
import com.project.house.rental.repository.RoomTypeRepository;
import com.project.house.rental.service.RoomTypeService;
import com.project.house.rental.specification.RoomTypeSpecification;
import com.project.house.rental.utils.HibernateFilterHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final HibernateFilterHelper hibernateFilterHelper;

    public RoomTypeServiceImpl(RoomTypeRepository roomTypeRepository, HibernateFilterHelper hibernateFilterHelper) {
        this.roomTypeRepository = roomTypeRepository;
        this.hibernateFilterHelper = hibernateFilterHelper;
    }

    @Override
    public List<RoomTypeDto> getAllRoomTypes() {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_ROOM_TYPE_FILTER);

        List<RoomType> roomTypeList = roomTypeRepository.findAll();

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_ROOM_TYPE_FILTER);

        return roomTypeList.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public RoomTypeDto getRoomTypeById(long id) {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_ROOM_TYPE_FILTER);

        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại phòng với id = " + id));

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_ROOM_TYPE_FILTER);

        return toDto(roomType);
    }

    @Override
    public RoomTypeDto createRoomType(RoomTypeDto roomTypeDto) {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_ROOM_TYPE_FILTER);

        RoomType existingRoomType = roomTypeRepository.findByNameIgnoreCase(roomTypeDto.getName());

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_ROOM_TYPE_FILTER);

        if (existingRoomType != null) {
            throw new RuntimeException("Loại phòng đã tồn tại");
        }

        RoomType roomType = toEntity(roomTypeDto);

        roomType = roomTypeRepository.save(roomType);

        return toDto(roomType);
    }

    @Override
    public RoomTypeDto updateRoomType(long id, RoomTypeDto roomTypeDto) {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_ROOM_TYPE_FILTER);

        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại phòng với id = " + id));

        RoomType existingRoomType = roomTypeRepository.findByNameIgnoreCase(roomTypeDto.getName());

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_ROOM_TYPE_FILTER);


        if (existingRoomType != null && existingRoomType.getId() != id) {
            throw new RuntimeException("Loại phòng đã tồn tại");
        }

        updateEntityFromDto(roomType, roomTypeDto);

        roomType = roomTypeRepository.save(roomType);

        return toDto(roomType);
    }

    @Override
    public void deleteRoomTypeById(long id) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy loại phòng với id = " + id));

        roomTypeRepository.deleteById(roomType.getId());
    }

    @Override
    public Map<String, Object> getAllRoomTypesWithParams(RoomTypeParams roomTypeParams) {
        Specification<RoomType> specification = RoomTypeSpecification.searchByName(roomTypeParams.getName());

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
}
