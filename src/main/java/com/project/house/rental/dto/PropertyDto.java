package com.project.house.rental.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PropertyDto {

    long id;

    @NotEmpty(message = "Vui lòng nhập tiêu đề")
    @Size(max = 255, message = "Tiêu đề có tối đa 255 kí tự")
    String title;

    String description;

    @NotEmpty(message = "Vui lòng nhập địa chỉ")
    @Size(max = 255, message = "Địa chỉ có tối đa 255 kí tự")
    String location;

    @NotNull(message = "Vui lòng nhập giá")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    @Digits(integer = 10, fraction = 2, message = "Định dạng giá không hợp lệ !")
    Double price;

    @DecimalMin(value = "0.0", inclusive = false, message = "Diện tích phải lớn hơn 0")
    @Digits(integer = 10, fraction = 2, message = "Định dạng diện tích không hợp lệ !")
    Double area;

    @Min(value = 0, message = "Số phòng phải lớn hơn hoặc bằng 0")
    Integer numRooms;

    @NotEmpty(message = "Vui lòng chọn trạng thái")
    String status;

    boolean isBlocked = Boolean.FALSE;

    long userId;

    String userName;

    long cityId;

    String cityName;

    long districtId;

    String districtName;

    long roomTypeId;

    String roomTypeName;

    List<String> amenities;

    List<String> propertyImages;
}