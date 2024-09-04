package com.project.house.rental.mapper.auth;

import com.project.house.rental.dto.auth.AuthorityDto;
import com.project.house.rental.entity.auth.Authority;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {

    AuthorityMapper INSTANCE = Mappers.getMapper(AuthorityMapper.class);

    AuthorityDto toDto(Authority authority);
}
