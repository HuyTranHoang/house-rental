package com.project.house.rental.mapper;

import com.project.house.rental.dto.TransactionDto;
import com.project.house.rental.entity.Transaction;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@DecoratedWith(TransactionMapperDecorator.class)
public interface TransactionMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "type", target = "transactionType")
    @Mapping(source = "status", target = "status")
    TransactionDto toDto(Transaction transaction);

    Transaction toEntity(TransactionDto transactionDto);
}