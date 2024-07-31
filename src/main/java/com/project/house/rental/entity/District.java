package com.project.house.rental.entity;


import com.project.house.rental.constant.FilterConstant;
import jakarta.persistence.*;
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
@Table(name = "districts")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE districts SET is_deleted = true WHERE id = ?")
@FilterDef(name = FilterConstant.DELETE_DISTRICT_FILTER, parameters = @ParamDef(name = FilterConstant.IS_DELETED, type = Boolean.class))
@Filter(name = FilterConstant.DELETE_DISTRICT_FILTER, condition = FilterConstant.CONDITION)
public class District extends BaseEntity {

    String name;

    @ManyToOne
    @JoinColumn(name = "city_id")
    City city;

    @OneToMany(mappedBy = "district")
    List<Property> properties;
}