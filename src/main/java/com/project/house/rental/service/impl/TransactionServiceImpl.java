package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.dto.PaymentRequest;
import com.project.house.rental.dto.TransactionDto;
import com.project.house.rental.dto.params.TransactionParams;
import com.project.house.rental.entity.Transaction;
import com.project.house.rental.entity.Transaction_;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.repository.TransactionRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.TransactionService;
import com.project.house.rental.specification.TransactionSpecification;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final JWTTokenProvider jwtTokenProvider;

    public TransactionServiceImpl(TransactionRepository transactionRepository, JWTTokenProvider jwtTokenProvider, HibernateFilterHelper hibernateFilterHelper, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, Object> getAllTransactionsWithParams(TransactionParams transactionParams) {
        Specification<Transaction> spec = TransactionSpecification.filterByUserId(transactionParams.getUserId())
                .and(TransactionSpecification.filterByStatus(transactionParams.getStatus()))
                .and(TransactionSpecification.filterByTransactionType(transactionParams.getTransactionType()))
                .and(TransactionSpecification.filterByAmount(transactionParams.getMinAmount(), transactionParams.getMaxAmount()));

        Sort sort = switch (transactionParams.getSortBy()) {
            case "amountDesc" -> Sort.by(Transaction_.AMOUNT).descending();
            case "amountAsc" -> Sort.by(Transaction_.AMOUNT);
            case "statusDesc" -> Sort.by(Transaction_.STATUS).descending();
            case "statusAsc" -> Sort.by(Transaction_.STATUS);
            case "transactionDateDesc" -> Sort.by(Transaction_.TRANSACTION_DATE).descending();
            case "transactionDateAsc" -> Sort.by(Transaction_.TRANSACTION_DATE);
            default -> Sort.by(Transaction_.ID).ascending();
        };

        if (transactionParams.getPageNumber() < 0) {
            transactionParams.setPageNumber(0);
        }

        if (transactionParams.getPageSize() <= 0) {
            transactionParams.setPageSize(10);
        }

        Pageable pageable = PageRequest.of(
                transactionParams.getPageNumber(),
                transactionParams.getPageSize(),
                sort
        );

        Page<Transaction> transactionPage = transactionRepository.findAll(spec, pageable);

        PageInfo pageInfo = new PageInfo(transactionPage);

        List<TransactionDto> transactionDtoList = transactionPage.stream()
                .map(this::toDto)
                .toList();

        return Map.of(
                "pageInfo", pageInfo,
                "data", transactionDtoList
        );
    }

    @Override
    public TransactionDto createTransaction(PaymentRequest paymentRequest, HttpServletRequest request) {
        String username = jwtTokenProvider.getUsernameFromToken(request);
        UserEntity currentUser = userRepository.findUserByUsername(username);

        if (currentUser == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản với username: " + username);
        }

        Transaction transaction = new Transaction();
        transaction.setUser(currentUser);
        transaction.setTransactionId("");
        transaction.setAmount(paymentRequest.getAmount());
        transaction.setTransactionDate(new Date());
        transaction.setStatus(Transaction.TransactionStatus.PENDING);
        if (paymentRequest.getType().equalsIgnoreCase("DEPOSIT")) {
            transaction.setType(Transaction.TransactionType.DEPOSIT);
        } else {
            throw new UsernameNotFoundException("Loại giao dịch không hợp lệ: [" + username + "]");
        }

        transactionRepository.save(transaction);

        return toDto(transaction);
    }

    @Override
    public void updateTransactionStatus(String txnRef, String status) {
        Transaction transaction = transactionRepository.findByTransactionId(txnRef);
        if (transaction != null) {
            transaction.setStatus(Transaction.TransactionStatus.valueOf(status));
            transactionRepository.save(transaction);
        }
    }


    @Override
    public TransactionDto findByTransactionId(String transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId);

        if (transaction == null) {
            throw new RuntimeException("Không tìm thấy giao dịch với transactionId: " + transactionId);
        }

        return toDto(transaction);
    }

    @Override
    public TransactionDto updateTransactionId(long id, String transactionId) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giao dịch"));

        transaction.setTransactionId(transactionId);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return toDto(updatedTransaction);
    }


    @Override
    public TransactionDto toDto(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .userId(transaction.getUser().getId())
                .username(transaction.getUser().getUsername())
                .transactionId(transaction.getTransactionId())
                .amount(transaction.getAmount())
                .transactionDate(transaction.getTransactionDate())
                .transactionType(String.valueOf(transaction.getType()))
                .status(String.valueOf(transaction.getStatus()))
                .description(transaction.getDescription())
                .build();
    }

    @Override
    public Transaction toEntity(TransactionDto transactionDto) {
        UserEntity user = userRepository.findById(transactionDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return Transaction.builder()
                .user(user)
                .transactionId(transactionDto.getTransactionId())
                .amount(transactionDto.getAmount())
                .transactionDate(transactionDto.getTransactionDate())
                .type(Transaction.TransactionType.valueOf(transactionDto.getTransactionType()))
                .status(Transaction.TransactionStatus.valueOf(transactionDto.getStatus()))
                .description(transactionDto.getDescription())
                .build();
    }

}
