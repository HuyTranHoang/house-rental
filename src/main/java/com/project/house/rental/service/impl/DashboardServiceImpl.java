package com.project.house.rental.service.impl;

import com.project.house.rental.entity.CommentReport;
import com.project.house.rental.entity.Property;
import com.project.house.rental.entity.Report;
import com.project.house.rental.entity.Transaction;
import com.project.house.rental.repository.*;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PropertyRepository propertyRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;
    private final CommentReportRepository commentReportRepository;

    public DashboardServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository, PropertyRepository propertyRepository, CommentRepository commentRepository, ReportRepository reportRepository, CommentReportRepository commentReportRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.propertyRepository = propertyRepository;
        this.commentRepository = commentRepository;
        this.reportRepository = reportRepository;
        this.commentReportRepository = commentReportRepository;
    }

    @Override
    public Map<String, Long> countEntitiesCreatedLastSevenMonths(String entityType) {
        LocalDate now = LocalDate.now();
        Map<String, Long> result = new HashMap<>();

        for (int i = 0; i < 8; i++) {
            LocalDate date = now.minusMonths(i);
            int month = date.getMonthValue();
            int year = date.getYear();
            long count = switch (entityType) {
                case "users" -> userRepository.countByCreatedAtMonthAndYear(month, year);
                case "properties" -> propertyRepository.countByCreatedAtMonthAndYear(month, year);
                case "comments" -> commentRepository.countByCreatedAtMonthAndYear(month, year);
                default -> throw new IllegalArgumentException("Invalid entity type: " + entityType);
            };

            result.put(date.getMonth().name(), count);
        }

        return result;
    }

    @Override
    public Map<String, Object> getStats(String period) {
        LocalDate now = LocalDate.now();
        Date startDate;
        Date endDate = Date.from(now.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        startDate = switch (period) {
            case "weekly" -> {
                LocalDate startOfWeek = now.with(java.time.DayOfWeek.MONDAY);
                yield Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
            case "monthly" -> {
                LocalDate startOfMonth = now.withDayOfMonth(1);
                yield Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
            case "total" -> new Date(0); // Epoch time
            default -> throw new IllegalArgumentException("Invalid period: " + period);
        };

        long userCount = userRepository.countByCreatedAtBetween(startDate, endDate);
        BigDecimal totalDepositAmount = transactionRepository.findTotalAmountByTransactionTypeAndDateBetween(Transaction.TransactionType.DEPOSIT, startDate, endDate);
        BigDecimal totalWithdrawalAmount = transactionRepository.findTotalAmountByTransactionTypeAndDateBetween(Transaction.TransactionType.WITHDRAWAL, startDate, endDate);
        long propertyCount = propertyRepository.countByCreatedAtBetween(startDate, endDate);
        long commentCount = commentRepository.countCommentsCreatedBetween(startDate, endDate);

        if (totalDepositAmount == null) {
            totalDepositAmount = BigDecimal.ZERO;
        }
        if (totalWithdrawalAmount == null) {
            totalWithdrawalAmount = BigDecimal.ZERO;
        }

        Map<String, Object> rs = new HashMap<>();
        rs.put("users", userCount);
        rs.put("deposits", totalDepositAmount);
        rs.put("withdrawals", totalWithdrawalAmount);
        rs.put("properties", propertyCount);
        rs.put("comments", commentCount);

        return rs;
    }

    @Override
    public Map<String, Long> countPendingStatuses() {
        long pendingProperties = propertyRepository.countPropertiesWithStatus(Property.PropertyStatus.PENDING);
        long pendingCommentReports = commentReportRepository.countReportsWithStatus(CommentReport.ReportStatus.PENDING);
        long pendingReports = reportRepository.countReportsWithStatus(Report.ReportStatus.PENDING);

        Map<String, Long> pendingCounts = new HashMap<>();
        pendingCounts.put("properties", pendingProperties);
        pendingCounts.put("commentReports", pendingCommentReports);
        pendingCounts.put("reports", pendingReports);

        return pendingCounts;
    }

    @Override
    public List<Map<String, Object>> countCreatedEntitiesLastSevenMonths() {
        LocalDate now = LocalDate.now();
        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = now.minusMonths(i);
            int month = date.getMonthValue();
            int year = date.getYear();

            long userCount = userRepository.countByCreatedAtMonthAndYear(month, year);
            long propertyCount = propertyRepository.countByCreatedAtMonthAndYear(month, year);
            long commentCount = commentRepository.countByCreatedAtMonthAndYear(month, year);

            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("month", date.getMonth().name());
            dataPoint.put("users", userCount);
            dataPoint.put("properties", propertyCount);
            dataPoint.put("comments", commentCount);

            result.add(dataPoint);
        }

        Collections.reverse(result);
        return result;
    }

    @Override
    public List<Map<String, Object>> getTotalTransactionAmountsLastSevenMonths() {
        LocalDate now = LocalDate.now();
        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 0; i <= 6; i++) {
            LocalDate date = now.minusMonths(i);
            int month = date.getMonthValue();
            int year = date.getYear();

            BigDecimal depositTotal = transactionRepository.sumAmountByTransactionTypeAndCreatedAtMonthAndYear(Transaction.TransactionType.DEPOSIT, month, year);
            BigDecimal withdrawalTotal = transactionRepository.sumAmountByTransactionTypeAndCreatedAtMonthAndYear(Transaction.TransactionType.WITHDRAWAL, month, year);

            Map<String, Object> dataPoint = Map.of(
                    "month", date.getMonth().name(),
                    "deposit", depositTotal != null ? depositTotal : BigDecimal.ZERO,
                    "withdrawal", withdrawalTotal != null ? withdrawalTotal : BigDecimal.ZERO
            );

            result.add(dataPoint);
        }
        Collections.reverse(result);
        return result;
    }

}