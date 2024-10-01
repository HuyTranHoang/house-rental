package com.project.house.rental.repository.auth;

import com.project.house.rental.entity.auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    UserEntity findUserById(long id);

    UserEntity findUserByUsername(String username);

    UserEntity findUserByEmail(String email);

    long countByCreatedAtBetween(Date startDate, Date endDate);

}
