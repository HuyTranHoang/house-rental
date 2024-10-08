package com.project.house.rental.service;

import com.project.house.rental.dto.RoomTypeDto;
import com.project.house.rental.dto.params.RoomTypeParams;

import java.util.List;
import java.util.Map;

public interface RoomTypeService {
    List<RoomTypeDto> getAllRoomTypes();

    RoomTypeDto getRoomTypeById(long id);

    RoomTypeDto createRoomType(RoomTypeDto roomTypeDto);

    RoomTypeDto updateRoomType(long id, RoomTypeDto roomTypeDto);

    void deleteRoomTypeById(long id);

    void deleteMultipleRoomTypes(List<Long> ids);

    Map<String, Object> getAllRoomTypesWithParams(RoomTypeParams roomTypeParams);
}
