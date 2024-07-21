package com.project.house.rental.repository.auth;

import com.project.house.rental.entity.auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserByUsername(String username);
    UserEntity findUserByEmail(String email);
}
