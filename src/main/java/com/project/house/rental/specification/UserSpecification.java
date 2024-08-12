package com.project.house.rental.specification;

import com.project.house.rental.entity.auth.Role;
import com.project.house.rental.entity.auth.Role_;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.entity.auth.UserEntity_;
import com.project.house.rental.repository.auth.RoleRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserSpecification {

    public static Specification<UserEntity> searchByUsernameEmailPhone(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(search))
                return cb.conjunction();

            return cb.or(
                    cb.like(cb.lower(root.get(UserEntity_.USERNAME)), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get(UserEntity_.EMAIL)), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get(UserEntity_.PHONE_NUMBER)), "%" + search.toLowerCase() + "%")
            );
        };
    }

    public static Specification<UserEntity> filterByRoles(String roleList, RoleRepository roleRepository) {
        return ((root, query, cb) -> {
            if (!StringUtils.hasLength(roleList))
                return cb.conjunction();

            Join<UserEntity, Role> userRoleJoin = root.join(UserEntity_.ROLES);
            List<Predicate> predicates = new ArrayList<>();
            String[] roles = roleList.split(",");
            Arrays.stream(roles).forEach(
                    categoryName -> {
                        Role role = roleRepository.findRoleByNameIgnoreCase(categoryName.trim());

                        if (role != null)
                            predicates.add(cb.equal(userRoleJoin.get(Role_.ID), role.getId()));
                    }
            );

            return cb.or(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<UserEntity> filterByIsNonLocked(Boolean isNonLocked) {
        return (root, query, cb) -> {
            if (isNonLocked == null)
                return cb.conjunction();

            return cb.equal(root.get(UserEntity_.IS_NON_LOCKED), isNonLocked);
        };
    }
}
