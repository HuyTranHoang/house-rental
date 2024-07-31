package com.project.house.rental.entity;

import com.project.house.rental.constant.FilterConstant;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cities")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE cities SET is_deleted = true WHERE id = ?")
@FilterDef(name = FilterConstant.DELETE_CITY_FILTER, parameters = @ParamDef(name = FilterConstant.IS_DELETED, type = Boolean.class))
@Filter(name = FilterConstant.DELETE_CITY_FILTER, condition = FilterConstant.CONDITION)
public class City extends BaseEntity {

    String name;

    @OneToMany(mappedBy = "city")
    List<District> districts;

    @OneToMany(mappedBy = "city")
    List<Property> properties;
}
