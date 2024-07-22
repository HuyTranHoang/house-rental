package com.project.house.rental.service;

import com.project.house.rental.dto.RoomTypeDto;
import com.project.house.rental.dto.params.RoomTypeParams;
import com.project.house.rental.entity.RoomType;

import java.util.Map;

public interface RoomTypeService extends GenericService<RoomType, RoomTypeDto> {
    Map<String, Object> getAllRoomTypesWithParams(RoomTypeParams roomTypeParams);
}
