package com.project.house.rental.repository;

import com.project.house.rental.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    Transaction findByTransactionId(String transactionId);

    List<Transaction> findAllByUserId(Long userId);

    List<Transaction> findAllByStatus(Transaction.TransactionStatus status);

    List<Transaction> findAllByTransactionDateBetween(Date startDate, Date endDate);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :transactionType AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal findTotalAmountByTransactionTypeAndDateBetween(@Param("transactionType") Transaction.TransactionType transactionType,
                                                          @Param("startDate") Date startDate,
                                                          @Param("endDate") Date endDate);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = :transactionType AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByTransactionTypeAndCreatedAtBetween(
            @Param("transactionType") Transaction.TransactionType transactionType,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :transactionType")
    BigDecimal getTotalAmountByTransactionType(@Param("transactionType") Transaction.TransactionType transactionType);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type AND EXTRACT(MONTH FROM t.transactionDate) = :month AND EXTRACT(YEAR FROM t.transactionDate) = :year")
    BigDecimal sumAmountByTransactionTypeAndCreatedAtMonthAndYear(@Param("type") Transaction.TransactionType type, @Param("month") int month, @Param("year") int year);
}
