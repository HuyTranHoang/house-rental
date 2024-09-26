package com.project.house.rental.entity;

import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.entity.auth.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "properties")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE properties SET is_deleted = true WHERE id = ?")
@FilterDef(name = FilterConstant.DELETE_PROPERTY_FILTER, parameters = @ParamDef(name = FilterConstant.IS_DELETED, type = Boolean.class))
@Filter(name = FilterConstant.DELETE_PROPERTY_FILTER, condition = FilterConstant.CONDITION)
@FilterDef(name = FilterConstant.BLOCK_PROPERTY_FILTER, parameters = @ParamDef(name = FilterConstant.IS_BLOCKED, type = Boolean.class))
@Filter(name = FilterConstant.BLOCK_PROPERTY_FILTER, condition = FilterConstant.BLOCKED_CONDITION)
@FilterDef(name = FilterConstant.STATUS_PROPERTY_FILTER, parameters = @ParamDef(name = FilterConstant.STATUS, type = String.class))
@Filter(name = FilterConstant.STATUS_PROPERTY_FILTER, condition = FilterConstant.STATUS_CONDITION)
public class Property extends BaseEntity {

    @Column(name = "title")
    String title;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "price")
    double price;

    @Column(name = "location")
    String location;

    @Column(name = "area")
    double area;

    @Column(name = "num_rooms")
    int numRooms;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('PENDING', 'RESOLVED','REJECTED') DEFAULT 'PENDING'")
    PropertyStatus status;

    @Column(name = "is_blocked")
    boolean isBlocked;

    @Column(name = "is_deleted")
    boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne
    @JoinColumn(name = "city_id")
    City city;

    @ManyToOne
    @JoinColumn(name = "district_id")
    District district;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    RoomType roomType;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "property_amenities",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    List<Amenity> amenities;

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY)
    List<Comment> comments;

    @OneToMany(mappedBy = "property")
    List<PropertyImage> propertyImages;

    @Column(name = "thumbnail_url")
    String thumbnailUrl;

    @Column(name = "thumbnail_blurhash")
    String thumbnailBlurhash;

    @OneToMany(mappedBy = "property")
    List<Favorite> favorites;

    @Column(name = "is_priority")
    boolean isPriority;

    @Column(name = "priority_expiration")
    Timestamp priorityExpiration;

    @Column(name = "refresh_day")
    Timestamp refreshDay;

    @Column(name = "is_hidden")
    boolean isHidden;

    public enum PropertyStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
