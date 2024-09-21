package com.project.house.rental.repository;


import com.project.house.rental.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long>, JpaSpecificationExecutor<Membership> {
    @Query("SELECT c FROM Membership c WHERE c.id = :id AND c.isDeleted = false")
    Membership findByIdWithFilter(long id);

    Membership findByNameIgnoreCase(String name);
}
