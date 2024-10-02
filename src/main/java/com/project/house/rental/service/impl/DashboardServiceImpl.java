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
    public long countUsersCreatedThisWeek() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(java.time.DayOfWeek.MONDAY);

        Date startDate = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(now.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        return userRepository.countByCreatedAtBetween(startDate, endDate);

    }

    @Override
    public long countUsersCreatedThisMonth() {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);

        Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(now.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        return userRepository.countByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public long countTotalUsers() {
        return userRepository.count();
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
    public BigDecimal getTotalDepositAmountThisWeek() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(java.time.DayOfWeek.MONDAY);

        Date startDate = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(now.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        BigDecimal totalAmount = transactionRepository.findTotalAmountByTransactionTypeAndDateBetween(Transaction.TransactionType.DEPOSIT, startDate, endDate);

        return totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalDepositAmountForCurrentMonth() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate now = LocalDate.now();

        Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(now.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        BigDecimal totalAmount = transactionRepository.sumAmountByTransactionTypeAndCreatedAtBetween(Transaction.TransactionType.DEPOSIT, startDate, endDate);

        return totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalDepositAmount() {
        BigDecimal totalAmount = transactionRepository.getTotalAmountByTransactionType(Transaction.TransactionType.DEPOSIT);
        return totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalWithdrawalAmountThisWeek() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(java.time.DayOfWeek.MONDAY);

        Date startDate = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(now.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        BigDecimal totalAmount = transactionRepository.findTotalAmountByTransactionTypeAndDateBetween(Transaction.TransactionType.WITHDRAWAL, startDate, endDate);

        return totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalWithdrawalAmountForCurrentMonth() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate now = LocalDate.now();

        Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(now.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        BigDecimal totalAmount = transactionRepository.sumAmountByTransactionTypeAndCreatedAtBetween(Transaction.TransactionType.WITHDRAWAL, startDate, endDate);

        return totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalWithdrawalAmount() {
        BigDecimal totalAmount = transactionRepository.getTotalAmountByTransactionType(Transaction.TransactionType.WITHDRAWAL);
        return totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }

    @Override
    public long countPropertiesCreatedThisWeek() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(java.time.DayOfWeek.MONDAY);

        Date startDate = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(now.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        return propertyRepository.countByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public long countPropertiesCreatedThisMonth() {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);

        Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(now.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        return propertyRepository.countByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public long countTotalProperties() {
        return propertyRepository.countTotalProperties();
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
    public long countCommentsCreatedThisWeek() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(java.time.DayOfWeek.MONDAY);

        Date startDate = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(now.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        return commentRepository.countCommentsCreatedBetween(startDate, endDate);
    }

    @Override
    public long countCommentsCreatedThisMonth() {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);

        Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(now.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        return commentRepository.countCommentsCreatedBetween(startDate, endDate);
    }

    @Override
    public long countTotalComments() {
        return commentRepository.count();
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
    public long countPropertiesWithPendingStatus() {
        return propertyRepository.countPropertiesWithStatus(Property.PropertyStatus.PENDING);
    }

    @Override
    public long countCommentReportsWithPendingStatus() {
        return commentReportRepository.countReportsWithStatus(CommentReport.ReportStatus.PENDING);
    }

    @Override
    public long countReportsWithPendingStatus() {
        return reportRepository.countReportsWithStatus(Report.ReportStatus.PENDING);
    }
}