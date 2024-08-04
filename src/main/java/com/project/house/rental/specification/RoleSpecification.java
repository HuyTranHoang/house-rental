package com.project.house.rental.specification;

import com.project.house.rental.entity.auth.Authority;
import com.project.house.rental.entity.auth.Authority_;
import com.project.house.rental.entity.auth.Role;
import com.project.house.rental.entity.auth.Role_;
import com.project.house.rental.repository.auth.AuthorityRepository;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class RoleSpecification {

    public static Specification<Role> searchByName(String name) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(name))
                return cb.conjunction();

            return cb.like(cb.lower(root.get(Role_.NAME)), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Role> filterByAuthority(String authorityList, AuthorityRepository authorityRepository) {
        return (root, query, cb) -> {
            if (!StringUtils.hasLength(authorityList)) {
                return cb.conjunction();
            }

            String[] authorities = authorityList.split(",");
            Set<Long> authorityIds = Arrays.stream(authorities)
                    .map(authorityPrivilege -> authorityRepository.findAuthorityByPrivilege(authorityPrivilege.trim()))
                    .filter(Objects::nonNull)
                    .map(Authority::getId)
                    .collect(Collectors.toSet());

            if (authorityIds.isEmpty()) {
                return cb.conjunction();
            }

            Join<Role, Authority> roleAuthorityJoin = root.join(Role_.AUTHORITIES);
            Expression<Long> countExpression = cb.countDistinct(roleAuthorityJoin.get(Authority_.ID));
            Predicate authorityPredicate = roleAuthorityJoin.get(Authority_.ID).in(authorityIds);

            query.groupBy(root.get(Role_.ID));
            query.having(cb.equal(countExpression, authorityIds.size()));

            return cb.and(authorityPredicate);
        };
    }


}
