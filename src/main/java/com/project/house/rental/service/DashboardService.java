package com.project.house.rental.service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DashboardService {
    Map<String, Long> countUsersCreatedLastSevenMonths();
    Map<String, Long> countPropertiesCreatedLastSevenMonths();
    Map<String, Long> countCommentsCreatedLastSevenMonths();

    Map<String, Object> getWeeklyStats();
    Map<String, Object> getMonthlyStats();
    Map<String, Object> getTotalStats();
    Map<String, Long> countPendingStatuses();
    List<Map<String, Object>> countCreatedEntitiesLastSevenMonths();
    List<Map<String, Object>> getTotalTransactionAmountsLastSevenMonths();
}
