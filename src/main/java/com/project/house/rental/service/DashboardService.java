package com.project.house.rental.service;


import java.util.List;
import java.util.Map;

public interface DashboardService {
    Map<String, Long> countEntitiesCreatedLastSevenMonths(String entityType);

    Map<String, Object> getStats(String period);

    Map<String, Long> countPendingStatuses();

    List<Map<String, Object>> countCreatedEntitiesLastSevenMonths();

    List<Map<String, Object>> getTotalTransactionAmountsLastSevenMonths();
}
