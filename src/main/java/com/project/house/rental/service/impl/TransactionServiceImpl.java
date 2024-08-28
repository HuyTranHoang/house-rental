package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
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

import java.util.List;
import java.util.Map;

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
                .and(TransactionSpecification.filterByAmount(transactionParams.getMinAmount(), transactionParams.getMaxAmount()));

        Sort sort = switch (transactionParams.getSortBy()) {
            case "amountDesc" -> Sort.by(Transaction_.AMOUNT).descending();
            case "amountAsc" -> Sort.by(Transaction_.AMOUNT).ascending();
            case "statusDesc" -> Sort.by(Transaction_.STATUS).descending();
            case "statusAsc" -> Sort.by(Transaction_.STATUS).ascending();
            default -> Sort.by(Transaction_.TRANSACTION_DATE).ascending();
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
    public TransactionDto createTransaction(TransactionDto transactionDto, HttpServletRequest request) {
        String username = jwtTokenProvider.getUsernameFromToken(request);
        UserEntity currentUser = userRepository.findUserByUsername(username);

        if (currentUser == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản với username: " + username);
        }

        transactionDto.setUserId(currentUser.getId());


        Transaction transaction = toEntity(transactionDto);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return toDto(savedTransaction);
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

    @Override
    public TransactionDto updateTransactionStatus(long transactionId, String status) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giao dịch"));

        try {
            Transaction.TransactionStatus newStatus = Transaction.TransactionStatus.valueOf(status.toUpperCase());
            transaction.setStatus(newStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Trạng thái giao dịch không hợp lệ: " + status);
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return toDto(updatedTransaction);
    }


}