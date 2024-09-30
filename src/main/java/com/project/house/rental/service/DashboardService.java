package com.project.house.rental.service;


import java.math.BigDecimal;

public interface DashboardService {
    long countUsersCreatedThisWeek();
    long countUsersCreatedThisMonth();
    long countTotalUsers();

    BigDecimal getTotalDepositAmountThisWeek();
    BigDecimal getTotalDepositAmountForCurrentMonth();
    BigDecimal getTotalDepositAmount();

    BigDecimal getTotalWithdrawalAmountThisWeek();
    BigDecimal getTotalWithdrawalAmountForCurrentMonth();
    BigDecimal getTotalWithdrawalAmount();

    long countPropertiesCreatedThisWeek();
    long countPropertiesCreatedThisMonth();
    long countTotalProperties();
}
