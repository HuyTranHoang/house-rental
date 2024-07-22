package com.project.house.rental.controller;

import com.project.house.rental.dto.CityDto;
import com.project.house.rental.dto.RoomTypeDto;
import com.project.house.rental.dto.params.RoomTypeParams;
import com.project.house.rental.service.RoomTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/room-type")
public class RoomTypeController {
    public final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @GetMapping({"/", ""})
    public ResponseEntity<Map<String, Object>> getAllRoomTypes(@ModelAttribute RoomTypeParams roomTypeParams) {
        Map<String, Object> roomTypesWithPagination = roomTypeService.getAllRoomTypesWithParams(roomTypeParams);
        return ResponseEntity.ok(roomTypesWithPagination);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoomTypeDto>> getAllRoomTypesNoPaging() {
        List<RoomTypeDto> roomTypeDtoList = roomTypeService.getAll();
        return ResponseEntity.ok(roomTypeDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeDto> getRoomTypeById(@PathVariable long id) {
        RoomTypeDto roomTypeDto = roomTypeService.getById(id);
        return ResponseEntity.ok(roomTypeDto);
    }

    @PostMapping({"/", ""})
    public ResponseEntity<RoomTypeDto> createRoomType(@RequestBody @Valid RoomTypeDto roomTypeDto) {
        RoomTypeDto roomType = roomTypeService.create(roomTypeDto);
        return ResponseEntity.ok(roomType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeDto> updateRoomType(@PathVariable long id, @RequestBody @Valid RoomTypeDto roomTypeDto) {
        RoomTypeDto roomType = roomTypeService.update(id, roomTypeDto);
        return ResponseEntity.ok(roomType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable long id) {
        roomTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
