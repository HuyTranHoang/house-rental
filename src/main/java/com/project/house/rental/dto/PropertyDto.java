package com.project.house.rental.dto;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Vị trí là bắt buộc")
    String location;

    @NotNull(message = "Bắt buộc có giá !")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    @Digits(integer = 10, fraction = 2, message = "Định dạng giá không hợp lệ !")
    Double price;

    @DecimalMin(value = "0.0", inclusive = false, message = "Diện tích phải lớn hơn 0")
    @Digits(integer = 10, fraction = 2, message = "Diện tích format không hợp lệ !")
    Double area;

    @Min(value = 0, message = "Số phòng phải lớn hơn hoặc bằng 0")
    Integer numRooms;

    @NotBlank(message = "Trạng thái là bắt buộc")
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