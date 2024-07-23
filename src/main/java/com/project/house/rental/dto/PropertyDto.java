package com.project.house.rental.dto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PropertyDto {

    long id;

    String title;

    String description;

    Double price;

    String location;

    Double area;

    Integer numRooms;

    String status;

    String userName ;
    long userId;

    String cityName;
    long cityId;

    String districtName;
    long districtId;

    String roomTypeName;
    long roomTypeId;

}