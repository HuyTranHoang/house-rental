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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    public Map<String, Map<String, Long>> countCreatedEntitiesLastSevenMonths() {
        LocalDate now = LocalDate.now();
        Map<String, Map<String, Long>> result = new HashMap<>();

        Map<String, Long> userCounts = new HashMap<>();
        Map<String, Long> propertyCounts = new HashMap<>();
        Map<String, Long> commentCounts = new HashMap<>();

        for (int i = 0; i < 8; i++) {
            LocalDate date = now.minusMonths(i);
            int month = date.getMonthValue();
            int year = date.getYear();

            long userCount = userRepository.countByCreatedAtMonthAndYear(month, year);
            userCounts.put(date.getMonth().name(), userCount);

            long propertyCount = propertyRepository.countByCreatedAtMonthAndYear(month, year);
            propertyCounts.put(date.getMonth().name(), propertyCount);

            long commentCount = commentRepository.countByCreatedAtMonthAndYear(month, year);
            commentCounts.put(date.getMonth().name(), commentCount);
        }

        result.put("users", userCounts);
        result.put("properties", propertyCounts);
        result.put("comments", commentCounts);

        return result;
    }


}