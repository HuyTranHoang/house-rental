package com.project.house.rental.entity;

import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.entity.compositeKey.FavoritePrimaryKey;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "favorites")
@FieldDefaults(level = AccessLevel.PRIVATE)
//@SQLDelete(sql = "UPDATE favorites SET is_deleted = true WHERE user_id = ? AND property_id = ?")
@FilterDef(name = FilterConstant.DELETE_FAVORITE_FILTER, parameters = @ParamDef(name = FilterConstant.IS_DELETED, type = Boolean.class))
@Filter(name = FilterConstant.DELETE_FAVORITE_FILTER, condition = FilterConstant.CONDITION)
public class Favorite {

    @EmbeddedId
    FavoritePrimaryKey favoritePrimaryKey;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne
    @MapsId("propertyId")
    @JoinColumn(name = "property_id")
    Property property;

    @Column(name = "is_deleted")
    boolean isDeleted = Boolean.FALSE;

    @Column(name = "created_at")
    @CreationTimestamp
    Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    Date updatedAt;
}
