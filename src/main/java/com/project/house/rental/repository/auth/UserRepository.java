package com.project.house.rental.repository.auth;

import com.project.house.rental.entity.auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    UserEntity findUserById(long id);

    UserEntity findUserByUsername(String username);

    UserEntity findUserByEmail(String email);

    long countByCreatedAtBetween(Date startDate, Date endDate);
    @Query("SELECT COUNT(u) FROM UserEntity u WHERE EXTRACT(MONTH FROM u.createdAt) = :month AND EXTRACT(YEAR FROM u.createdAt) = :year")
    long countByCreatedAtMonthAndYear(@Param("month") int month, @Param("year") int year);
}
