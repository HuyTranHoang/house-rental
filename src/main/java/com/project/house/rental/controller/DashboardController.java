package com.project.house.rental.controller;


import com.project.house.rental.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/users/week")
    public ResponseEntity<Long> getUsersCreatedThisWeek() {
        long userCount = dashboardService.countUsersCreatedThisWeek();
        return ResponseEntity.ok(userCount);
    }

    @GetMapping("/users/month")
    public ResponseEntity<Long> getUsersCreatedThisMonth() {
        long count = dashboardService.countUsersCreatedThisMonth();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/users/total")
    public ResponseEntity<Long> getTotalUsers() {
        long totalUsers = dashboardService.countTotalUsers();
        return ResponseEntity.ok(totalUsers);
    }

    @GetMapping("/deposit/week")
    public ResponseEntity<BigDecimal> getTotalDepositAmountThisWeek() {
        BigDecimal totalDepositAmount = dashboardService.getTotalDepositAmountThisWeek();
        return ResponseEntity.ok(totalDepositAmount);
    }

    @GetMapping("/deposit/month")
    public ResponseEntity<BigDecimal> getTotalDepositAmountForCurrentMonth() {
        BigDecimal totalDepositAmount = dashboardService.getTotalDepositAmountForCurrentMonth();
        return ResponseEntity.ok(totalDepositAmount);
    }

    @GetMapping("/deposit/total")
    public ResponseEntity<BigDecimal> getTotalDepositAmount() {
        BigDecimal totalAmount = dashboardService.getTotalDepositAmount();
        return ResponseEntity.ok(totalAmount);
    }
    @GetMapping("/withdrawal/week")
    public ResponseEntity<BigDecimal> getTotalWithdrawalAmountThisWeek() {
        BigDecimal totalDepositAmount = dashboardService.getTotalWithdrawalAmountThisWeek();
        return ResponseEntity.ok(totalDepositAmount);
    }

    @GetMapping("/withdrawal/month")
    public ResponseEntity<BigDecimal> getTotalWithdrawalAmountForCurrentMonth() {
        BigDecimal totalDepositAmount = dashboardService.getTotalWithdrawalAmountForCurrentMonth();
        return ResponseEntity.ok(totalDepositAmount);
    }

    @GetMapping("/withdrawal/total")
    public ResponseEntity<BigDecimal> getTotalWithdrawalAmount() {
        BigDecimal totalAmount = dashboardService.getTotalWithdrawalAmount();
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/properties/week")
    public ResponseEntity<Long> countPropertiesCreatedThisWeek() {
        long count = dashboardService.countPropertiesCreatedThisWeek();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/properties/month")
    public ResponseEntity<Long> countPropertiesCreatedThisMonth() {
        long count = dashboardService.countPropertiesCreatedThisMonth();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/properties/total")
    public ResponseEntity<Long> countTotalProperties() {
        long totalCount = dashboardService.countTotalProperties();
        return ResponseEntity.ok(totalCount);
    }

    @GetMapping("/comments/week")
    public ResponseEntity<Long> countCommentsCreatedThisWeek() {
        long totalCount = dashboardService.countCommentsCreatedThisWeek();
        return ResponseEntity.ok(totalCount);
    }
    @GetMapping("/comments/month")
    public ResponseEntity<Long> countCommentsCreatedThisMonth() {
        long totalCount = dashboardService.countCommentsCreatedThisMonth();
        return ResponseEntity.ok(totalCount);
    }

    @GetMapping("/comments/total")
    public ResponseEntity<Long> countTotalComments() {
        long totalCount = dashboardService.countTotalComments();
        return ResponseEntity.ok(totalCount);
    }

    @GetMapping("/users/last-seven-months")
    public ResponseEntity<Map<String, Long>> getTotalUsersCreatedLastFiveMonths() {
        Map<String, Long> counts = dashboardService.countUsersCreatedLastSevenMonths();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/properties/last-seven-months")
    public ResponseEntity<Map<String, Long>> getCountOfPropertiesCreatedLastFiveMonths() {
        Map<String, Long> counts = dashboardService.countPropertiesCreatedLastSevenMonths();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/comments/last-seven-months")
    public ResponseEntity<Map<String, Long>> getCountOfCommentsCreatedLastFiveMonths() {
        Map<String, Long> counts = dashboardService.countCommentsCreatedLastSevenMonths();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/properties/pending")
    public ResponseEntity<Long> countPropertiesWithPendingStatus() {
        long totalCount = dashboardService.countPropertiesWithPendingStatus();
        return ResponseEntity.ok(totalCount);
    }
    @GetMapping("/comment-reports/pending")
    public ResponseEntity<Long> countCommentReportsWithPendingStatus() {
        long totalCount = dashboardService.countCommentReportsWithPendingStatus();
        return ResponseEntity.ok(totalCount);
    }
    @GetMapping("/reports/pending")
    public ResponseEntity<Long> countReportsWithPendingStatus() {
        long totalCount = dashboardService.countReportsWithPendingStatus();
        return ResponseEntity.ok(totalCount);
    }
}
