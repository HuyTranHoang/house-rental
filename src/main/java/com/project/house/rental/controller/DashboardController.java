package com.project.house.rental.controller;


import com.project.house.rental.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats(@RequestParam String period) {
        Map<String, Object> data = dashboardService.getStats(period);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/last-seven-months")
    public ResponseEntity<Map<String, Long>> getCountCreatedEntitiesLastSevenMonths(@RequestParam String entityType) {
        Map<String, Long> counts = dashboardService.countEntitiesCreatedLastSevenMonths(entityType);
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/last-seven-months/all")
    public ResponseEntity<List<Map<String, Object>>> getCountCreatedEntitiesLastSevenMonths() {
        List<Map<String, Object>> counts = dashboardService.countCreatedEntitiesLastSevenMonths();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/pending")
    public ResponseEntity<Map<String, Long>> GetCountPendingStatuses() {
        Map<String, Long> counts = dashboardService.countPendingStatuses();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/transaction")
    public List<Map<String, Object>> getTotalTransactionAmounts() {
        return dashboardService.getTotalTransactionAmountsLastSevenMonths();
    }
}
