package com.project.house.rental.entity;

import com.project.house.rental.entity.auth.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reports")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE reports SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Report extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_id")
    Property property;

    @Column(name = "reason", columnDefinition = "TEXT")
    String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    ReportStatus status = ReportStatus.PENDING;

    @Column(name = "is_deleted")
    boolean isDeleted = Boolean.FALSE;

    @Column(name = "created_at")
    @CreationTimestamp
    Date createdAt;

    public enum ReportStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

}
