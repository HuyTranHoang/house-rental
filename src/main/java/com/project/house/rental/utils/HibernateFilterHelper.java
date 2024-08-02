package com.project.house.rental.utils;

import com.project.house.rental.constant.FilterConstant;
import jakarta.persistence.EntityManager;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
public class HibernateFilterHelper {

    private final EntityManager entityManager;

    public HibernateFilterHelper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void enableFilter(String filterName) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter(filterName);
        if (FilterConstant.DELETE_PROPERTY_FILTER.equals(filterName)) {
            filter.setParameter(FilterConstant.IS_DELETED, false);
        } else if (FilterConstant.BLOCK_PROPERTY_FILTER.equals(filterName)) {
            filter.setParameter(FilterConstant.IS_BLOCKED, false);
        } else if (FilterConstant.STATUS_PROPERTY_FILTER.equals(filterName)) {
            filter.setParameter(FilterConstant.STATUS, "RESOLVED");
        }
    }

    public void disableFilter(String filterName) {
        Session session = entityManager.unwrap(Session.class);
        session.disableFilter(filterName);
    }
}
