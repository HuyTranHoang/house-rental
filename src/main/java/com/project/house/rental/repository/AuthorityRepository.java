package com.project.house.rental.repository;

import com.project.house.rental.entity.auth.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
