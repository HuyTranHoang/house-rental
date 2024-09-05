package com.project.house.rental.service.impl;

import com.project.house.rental.common.PageInfo;
import com.project.house.rental.dto.PaymentRequest;
import com.project.house.rental.dto.TransactionDto;
import com.project.house.rental.dto.params.TransactionParams;
import com.project.house.rental.entity.Transaction;
import com.project.house.rental.entity.Transaction_;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.exception.CustomRuntimeException;
import com.project.house.rental.repository.TransactionRepository;
import com.project.house.rental.repository.auth.UserRepository;
import com.project.house.rental.security.JWTTokenProvider;
import com.project.house.rental.service.TransactionService;
import com.project.house.rental.specification.TransactionSpecification;
import com.project.house.rental.utils.HibernateFilterHelper;
import jakarta.persistence.NoResultException;
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
            case "transactionTypeDesc" -> Sort.by(Transaction_.TYPE).descending();
            case "transactionTypeAsc" -> Sort.by(Transaction_.TYPE);
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
    public TransactionDto createTransaction(PaymentRequest paymentRequest, HttpServletRequest request) throws CustomRuntimeException {
        String username = jwtTokenProvider.getUsernameFromToken(request);
        UserEntity currentUser = userRepository.findUserByUsername(username);

        if (currentUser == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản với username: " + username);
        }

        Transaction transaction = Transaction.builder()
                .user(currentUser)
                .transactionId("")
                .amount(paymentRequest.getAmount())
                .transactionDate(new Date())
                .status(Transaction.TransactionStatus.PENDING)
                .description(paymentRequest.getDescription())
                .build();

        if (paymentRequest.getType().equalsIgnoreCase("DEPOSIT")) {
            transaction.setType(Transaction.TransactionType.DEPOSIT);
        } else if (paymentRequest.getType().equalsIgnoreCase("WITHDRAWAL")) {
            transaction.setType(Transaction.TransactionType.WITHDRAWAL);
        } else {
            throw new CustomRuntimeException("Loại giao dịch không hợp lệ");
        }

        transactionRepository.save(transaction);

        return toDto(transaction);
    }

    @Override
    public TransactionDto updateTransactionStatus(String txnRef, String status) {
        Transaction transaction = transactionRepository.findByTransactionId(txnRef);

        if (transaction != null) {
            transaction.setStatus(Transaction.TransactionStatus.valueOf(status));
            transactionRepository.save(transaction);
            return toDto(transaction);
        } else {
            throw new NoResultException("Không tìm thấy giao dịch với mã: " + txnRef);
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

    public Map<String, Object> getUserTransactions(HttpServletRequest request, TransactionParams transactionParams) {
        String username = jwtTokenProvider.getUsernameFromToken(request);
        UserEntity currentUser = userRepository.findUserByUsername(username);

        if (currentUser == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản với username: " + username);
        }

        transactionParams.setUserId(currentUser.getId());

        return getAllTransactionsWithParams(transactionParams);
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
