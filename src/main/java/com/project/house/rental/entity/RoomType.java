package com.project.house.rental.entity;

import com.project.house.rental.constant.FilterConstant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_types")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE room_types SET is_deleted = true WHERE id = ?")
@FilterDef(name = FilterConstant.DELETE_ROOM_TYPE_FILTER, parameters = @ParamDef(name = FilterConstant.IS_DELETED, type = Boolean.class))
@Filter(name = FilterConstant.DELETE_ROOM_TYPE_FILTER, condition = FilterConstant.CONDITION)
public class RoomType extends BaseEntity{

    @Column(name = "name", nullable = false)
    String name;

    @OneToMany(mappedBy = "roomType")
    List<Property> properties;
}
