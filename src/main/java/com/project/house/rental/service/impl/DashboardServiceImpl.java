package com.project.house.rental.service.impl;

import com.project.house.rental.entity.Transaction;
import com.project.house.rental.repository.TransactionRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public DashboardServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
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
}