package com.project.house.rental.repository;

import com.project.house.rental.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRespository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId ORDER BY n.createdAt DESC LIMIT 5")
    List<Notification> getNotificationsByUserId(long userId);
}
