package com.project.house.rental.repository;

import com.project.house.rental.entity.UserMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMembershipRepository extends JpaRepository<UserMembership, Long>, JpaSpecificationExecutor<UserMembership> {
//    @Query("SELECT d FROM UserMembership d WHERE d.id = :id AND d.isDeleted = false")
//    UserMembership findByIdWithFilter(long id);

    UserMembership findByUserId(long userId);
}
