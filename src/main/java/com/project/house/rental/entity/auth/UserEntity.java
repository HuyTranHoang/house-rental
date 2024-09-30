package com.project.house.rental.entity.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.entity.*;
import jakarta.persistence.*;
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
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@FilterDef(name = FilterConstant.DELETE_USER_FILTER, parameters = @ParamDef(name = FilterConstant.IS_DELETED, type = Boolean.class))
@Filter(name = FilterConstant.DELETE_USER_FILTER, condition = FilterConstant.CONDITION)
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

    @Column(name = "balance")
    double balance;

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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Property> properties;

    @OneToOne(mappedBy = "user")
    PasswordReset passwordReset;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Comment> comments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Favorite> favorites;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Transaction> transactions;

    @OneToOne(mappedBy = "user")
    UserMembership userMembership;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Report> reports;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<CommentReport> commentReports;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notification> notifications;
}
