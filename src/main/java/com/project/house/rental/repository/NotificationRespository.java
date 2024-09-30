package com.project.house.rental.repository;

import com.project.house.rental.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRespository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {
}
