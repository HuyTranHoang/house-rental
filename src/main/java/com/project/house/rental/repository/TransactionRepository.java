package com.project.house.rental.repository;

import com.project.house.rental.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    Transaction findByTransactionId(String transactionId);

    List<Transaction> findAllByUserId(Long userId);

    List<Transaction> findAllByStatus(Transaction.TransactionStatus status);

    List<Transaction> findAllByTransactionDateBetween(Date startDate, Date endDate);
}
