package com.project.house.rental.repository.auth;

import com.project.house.rental.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(String name);

    @Query("SELECT r FROM Role r WHERE r.id = :id AND r.isDeleted = false")
    Role findByIdWithFilter(long id);
}
