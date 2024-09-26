package com.project.house.rental.entity;

import com.project.house.rental.constant.FilterConstant;
import com.project.house.rental.entity.auth.UserEntity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
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
@Table(name = "comment_reports")
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE comment_reports SET is_deleted = true WHERE id = ?")
@FilterDef(name = FilterConstant.DELETE_COMMENT_REPORT_FILTER, parameters = @ParamDef(name = FilterConstant.IS_DELETED, type = Boolean.class))
@Filter(name = FilterConstant.DELETE_COMMENT_REPORT_FILTER, condition = FilterConstant.CONDITION)
public class CommentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    Comment comment;

    @Column(name = "reason", columnDefinition = "TEXT")
    String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    ReportCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    ReportStatus status;

    @Column(name = "is_deleted")
    boolean isDeleted;

    @Column(name = "created_at")
    @CreationTimestamp
    Date createdAt;

    public enum ReportStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    public enum ReportCategory {
        SCAM,
        INAPPROPRIATE_CONTENT,
        DUPLICATE,
        MISINFORMATION,
        OTHER
    }

}
