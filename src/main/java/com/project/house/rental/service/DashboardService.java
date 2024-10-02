package com.project.house.rental.service;


import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService {
    long countUsersCreatedThisWeek();
    long countUsersCreatedThisMonth();
    long countTotalUsers();
    Map<String, Long> countUsersCreatedLastSevenMonths();

    BigDecimal getTotalDepositAmountThisWeek();
    BigDecimal getTotalDepositAmountForCurrentMonth();
    BigDecimal getTotalDepositAmount();

    BigDecimal getTotalWithdrawalAmountThisWeek();
    BigDecimal getTotalWithdrawalAmountForCurrentMonth();
    BigDecimal getTotalWithdrawalAmount();

    long countPropertiesCreatedThisWeek();
    long countPropertiesCreatedThisMonth();
    long countTotalProperties();
    Map<String, Long> countPropertiesCreatedLastSevenMonths();

    long countCommentsCreatedThisWeek();
    long countCommentsCreatedThisMonth();
    long countTotalComments();
    Map<String, Long> countCommentsCreatedLastSevenMonths();

    long countPropertiesWithPendingStatus();
}
