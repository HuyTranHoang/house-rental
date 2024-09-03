package com.project.house.rental.mapper;

import com.project.house.rental.dto.TransactionDto;
import com.project.house.rental.entity.Transaction;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class TransactionMapperDecorator implements TransactionMapper {

    @Autowired
    @Qualifier("delegate")
    private TransactionMapper delegate;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Transaction toEntity(TransactionDto transactionDto) {
        Transaction transaction = delegate.toEntity(transactionDto);

        UserEntity user = userRepository.findById(transactionDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + transactionDto.getUserId()));

        transaction.setUser(user);
        return transaction;
    }

}