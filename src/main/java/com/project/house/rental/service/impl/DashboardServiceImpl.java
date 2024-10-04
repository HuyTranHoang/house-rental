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
    public Map<String, Long> countUsersCreatedLastSevenMonths() {
        LocalDate now = LocalDate.now();
        Map<String, Long> result = new HashMap<>();

        for (int i = 0; i < 8; i++) {
            LocalDate date = now.minusMonths(i);
            int month = date.getMonthValue();
            int year = date.getYear();
            long count = userRepository.countByCreatedAtMonthAndYear(month, year);
            result.put(date.getMonth().name(), count);
        }

        return result;
    }
    @Override
    public Map<String, Long> countPropertiesCreatedLastSevenMonths() {
        LocalDate now = LocalDate.now();
        Map<String, Long> result = new HashMap<>();

        for (int i = 0; i < 8; i++) {
            LocalDate date = now.minusMonths(i);
            int month = date.getMonthValue();
            int year = date.getYear();
            long count = propertyRepository.countByCreatedAtMonthAndYear(month, year);
            result.put(date.getMonth().name(), count);
        }

        return result;
    }
    @Override
    public Map<String, Long> countCommentsCreatedLastSevenMonths() {
        LocalDate now = LocalDate.now();
        Map<String, Long> result = new HashMap<>();

        for (int i = 0; i < 8; i++) {
            LocalDate date = now.minusMonths(i);
            int month = date.getMonthValue();
            int year = date.getYear();
            long count = commentRepository.countByCreatedAtMonthAndYear(month, year);
            result.put(date.getMonth().name(), count);
        }

        return result;
    }
    @Override
    public Map<String, Object> getWeeklyStats() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(java.time.DayOfWeek.MONDAY);

        Date startDate = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(now.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

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
    public Map<String, Object> getMonthlyStats() {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);

        Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(now.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        long userCount = userRepository.countByCreatedAtBetween(startDate, endDate);
        BigDecimal totalDepositAmount = transactionRepository.sumAmountByTransactionTypeAndCreatedAtBetween(Transaction.TransactionType.DEPOSIT, startDate, endDate);
        BigDecimal totalWithdrawalAmount = transactionRepository.sumAmountByTransactionTypeAndCreatedAtBetween(Transaction.TransactionType.WITHDRAWAL, startDate, endDate);
        long propertyCount = propertyRepository.countByCreatedAtBetween(startDate, endDate);
        long commentCount = commentRepository.countCommentsCreatedBetween(startDate, endDate);

        Map<String, Object> rs = new HashMap<>();
        rs.put("users", userCount);
        rs.put("deposits", totalDepositAmount != null ? totalDepositAmount : BigDecimal.ZERO);
        rs.put("withdrawals", totalWithdrawalAmount != null ? totalWithdrawalAmount : BigDecimal.ZERO);
        rs.put("properties", propertyCount);
        rs.put("comments", commentCount);

        return rs;
    }

    @Override
    public Map<String, Object> getTotalStats() {
        long totalUsers = userRepository.count();
        BigDecimal totalDepositAmount = transactionRepository.getTotalAmountByTransactionType(Transaction.TransactionType.DEPOSIT);
        BigDecimal totalWithdrawalAmount = transactionRepository.getTotalAmountByTransactionType(Transaction.TransactionType.WITHDRAWAL);
        long totalProperties = propertyRepository.countTotalProperties();
        long totalComments = commentRepository.count();

        Map<String, Object> rs = new HashMap<>();
        rs.put("users", totalUsers);
        rs.put("deposits", totalDepositAmount != null ? totalDepositAmount : BigDecimal.ZERO);
        rs.put("withdrawals", totalWithdrawalAmount != null ? totalWithdrawalAmount : BigDecimal.ZERO);
        rs.put("properties", totalProperties);
        rs.put("comments", totalComments);

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

        long[] userCounts = new long[7];
        long[] propertyCounts = new long[7];
        long[] commentCounts = new long[7];
        String[] months = new String[7];

        for (int i = 0; i < 7; i++) {
            LocalDate date = now.minusMonths(i);
            int month = date.getMonthValue();
            int year = date.getYear();

            userCounts[i] = userRepository.countByCreatedAtMonthAndYear(month, year);
            propertyCounts[i] = propertyRepository.countByCreatedAtMonthAndYear(month, year);
            commentCounts[i] = commentRepository.countByCreatedAtMonthAndYear(month, year);
            months[i] = date.getMonth().name();
        }

        for (int i = 6; i >= 0; i--) {
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("months", months[i]);
            dataPoint.put("users", userCounts[i]);
            dataPoint.put("properties", propertyCounts[i]);
            dataPoint.put("comments", commentCounts[i]);
            result.add(dataPoint);
        }

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