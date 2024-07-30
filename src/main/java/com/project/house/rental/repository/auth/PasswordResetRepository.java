package com.project.house.rental.repository.auth;

import com.project.house.rental.entity.auth.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    PasswordReset findByToken(String token);

    @Query(value = "SELECT * FROM password_resets p WHERE p.user_id = ?1", nativeQuery = true)
    PasswordReset findByUserId(long id);
}
