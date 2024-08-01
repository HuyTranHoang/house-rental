package com.project.house.rental.entity;

import com.project.house.rental.constant.FilterConstant;
import jakarta.persistence.*;
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
@Table(name = "property_images")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE property_images SET is_deleted = true WHERE id = ?")
@FilterDef(name = FilterConstant.DELETE_PROPERTY_IMAGE_FILTER, parameters = @ParamDef(name = FilterConstant.IS_DELETED, type = Boolean.class))
@Filter(name = FilterConstant.DELETE_PROPERTY_IMAGE_FILTER, condition = FilterConstant.CONDITION)
public class PropertyImage extends BaseEntity {

    String imageUrl;

    @Column(name = "public_id")
    String publicId;

    @ManyToOne
    @JoinColumn(name = "property_id")
    Property property;
}
