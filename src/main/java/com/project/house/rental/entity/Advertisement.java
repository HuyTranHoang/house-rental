package com.project.house.rental.entity;

import com.project.house.rental.constant.FilterConstant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "advertisements")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE advertisements SET is_deleted = true WHERE id = ?")
@FilterDef(name = FilterConstant.DELETE_ADVERTISEMENT_FILTER, parameters = @ParamDef(name = FilterConstant.IS_DELETED, type = Boolean.class))
@Filter(name = FilterConstant.DELETE_ADVERTISEMENT_FILTER, condition = FilterConstant.CONDITION)
public class Advertisement extends BaseEntity {
    @Column(name = "name")
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "image_url")
    String imageUrl;
    @Column(name = "is_actived")
    boolean isActived;
}

