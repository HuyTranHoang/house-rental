package com.project.house.rental.controller;


import com.project.house.rental.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/week")
    public ResponseEntity<Map<String, Object>> getWeeklyStats() {
        Map<String, Object> data = dashboardService.getWeeklyStats();
        return ResponseEntity.ok(data);
    }
    @GetMapping("/month")
    public ResponseEntity<Map<String, Object>> getMonthlyStats() {
        Map<String, Object> data = dashboardService.getMonthlyStats();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/total")
    public ResponseEntity<Map<String, Object>> getTotalStats() {
        Map<String, Object> data = dashboardService.getTotalStats();
        return ResponseEntity.ok(data);
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

    @GetMapping("/pending")
    public ResponseEntity<Map<String, Long>> GetCountPendingStatuses() {
        Map<String, Long> counts = dashboardService.countPendingStatuses();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/last-seven-months")
    public ResponseEntity<List<Map<String, Object>>> getCountCreatedEntitiesLastSevenMonths() {
        List<Map<String, Object>> counts = dashboardService.countCreatedEntitiesLastSevenMonths();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/transaction")
    public List<Map<String, Object>> getTotalTransactionAmounts() {
        return dashboardService.getTotalTransactionAmountsLastSevenMonths();
    }
}
