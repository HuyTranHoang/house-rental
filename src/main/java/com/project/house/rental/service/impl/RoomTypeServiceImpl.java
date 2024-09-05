package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.dto.RoomTypeDto;
import com.project.house.rental.dto.params.RoomTypeParams;
import com.project.house.rental.entity.RoomType;
import com.project.house.rental.entity.RoomType_;
import com.project.house.rental.exception.ConflictException;
import com.project.house.rental.mapper.RoomTypeMapper;
import com.project.house.rental.repository.RoomTypeRepository;
import com.project.house.rental.service.RoomTypeService;
import com.project.house.rental.specification.RoomTypeSpecification;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
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
                .map(RoomTypeMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public RoomTypeDto getRoomTypeById(long id) {

        RoomType roomType = roomTypeRepository.findByIdWithFilter(id);

        if (roomType == null) {
            throw new NoResultException("Không tìm thấy loại phòng với id = " + id);
        }

        return RoomTypeMapper.INSTANCE.toDto(roomType);
    }

    @Override
    public RoomTypeDto createRoomType(RoomTypeDto roomTypeDto) {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_ROOM_TYPE_FILTER);

        RoomType existingRoomType = roomTypeRepository.findByNameIgnoreCase(roomTypeDto.getName());

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_ROOM_TYPE_FILTER);

        if (existingRoomType != null) {
            throw new ConflictException("Loại phòng đã tồn tại");
        }

        RoomType roomType = RoomTypeMapper.INSTANCE.toEntity(roomTypeDto);

        roomType = roomTypeRepository.save(roomType);

        return RoomTypeMapper.INSTANCE.toDto(roomType);
    }

    @Override
    public RoomTypeDto updateRoomType(long id, RoomTypeDto roomTypeDto) {
        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_ROOM_TYPE_FILTER);

        RoomType roomType = roomTypeRepository.findByIdWithFilter(id);

        if (roomType == null) {
            throw new NoResultException("Không tìm thấy loại phòng với id = " + id);
        }

        RoomType existingRoomType = roomTypeRepository.findByNameIgnoreCase(roomTypeDto.getName());

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_ROOM_TYPE_FILTER);


        if (existingRoomType != null && existingRoomType.getId() != id) {
            throw new ConflictException("Loại phòng đã tồn tại");
        }

        RoomTypeMapper.INSTANCE.updateFromDto(roomTypeDto, roomType);

        roomType = roomTypeRepository.save(roomType);

        return RoomTypeMapper.INSTANCE.toDto(roomType);
    }

    @Override
    public void deleteRoomTypeById(long id) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy loại phòng với id = " + id));

        roomTypeRepository.deleteById(roomType.getId());
    }

    @Override
    public void deleteMultipleRoomTypes(List<Long> ids) {
        List<RoomType> roomTypeList = roomTypeRepository.findAllById(ids);
        roomTypeRepository.deleteAll(roomTypeList);
    }

    @Override
    public Map<String, Object> getAllRoomTypesWithParams(RoomTypeParams roomTypeParams) {
        Specification<RoomType> specification = RoomTypeSpecification.searchByName(roomTypeParams.getName());

        Sort sort = switch (roomTypeParams.getSortBy()) {
            case "nameAsc" -> Sort.by(RoomType_.NAME);
            case "nameDesc" -> Sort.by(RoomType_.NAME).descending();
            case "createdAtAsc" -> Sort.by(RoomType_.CREATED_AT);
            case "createdAtDesc" -> Sort.by(RoomType_.CREATED_AT).descending();
            default -> Sort.by(RoomType_.ID).descending();
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

        hibernateFilterHelper.enableFilter(FilterConstant.DELETE_ROOM_TYPE_FILTER);

        Page<RoomType> roomTypePage = roomTypeRepository.findAll(specification, pageable);

        hibernateFilterHelper.disableFilter(FilterConstant.DELETE_ROOM_TYPE_FILTER);

        PageInfo pageInfo = new PageInfo(roomTypePage);

        List<RoomTypeDto> roomTypeDtoList = roomTypePage.stream()
                .map(RoomTypeMapper.INSTANCE::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", roomTypeDtoList
        );
    }

}
