package com.project.house.rental.repository.auth;

import com.project.house.rental.entity.auth.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findAuthorityByPrivilege(String privilege);

    List<Authority> findAllByPrivilegeIn(List<String> authorityPrivileges);
}
