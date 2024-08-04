package com.project.house.rental.entity.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.house.rental.entity.BaseEntity;
import com.project.house.rental.entity.Favorite;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.Review;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity extends BaseEntity {

    String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    String email;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "avatar_url")
    String avatarUrl;

    @Column(name = "is_active")
    boolean isActive;

    @Column(name = "is_non_locked")
    boolean isNonLocked;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    List<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )

    List<Authority> authorities;

    @OneToMany(mappedBy = "user")
    List<Property> properties;

    @OneToOne(mappedBy = "user")
    PasswordReset passwordReset;

    @OneToMany(mappedBy = "user")
    List<Review> reviews;

    @OneToMany(mappedBy = "user")
    List<Favorite> favorites;
}
