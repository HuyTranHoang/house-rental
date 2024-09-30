package com.project.house.rental.controller;


import com.project.house.rental.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/users/this-week")
    public ResponseEntity<Long> getUsersCreatedThisWeek() {
        long userCount = dashboardService.countUsersCreatedThisWeek();
        return ResponseEntity.ok(userCount);
    }

    @GetMapping("/users/this-month")
    public ResponseEntity<Long> getUsersCreatedThisMonth() {
        long count = dashboardService.countUsersCreatedThisMonth();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/users/total")
    public ResponseEntity<Long> getTotalUsers() {
        long totalUsers = dashboardService.countTotalUsers();
        return ResponseEntity.ok(totalUsers);
    }
}
