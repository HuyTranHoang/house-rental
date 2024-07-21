package com.project.house.rental.service.auth;

import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.exception.CustomRuntimeException;


public interface UserService {
    UserEntityDto addNewUser(UserEntityDto user, String[] role);

    UserEntityDto updateUser(UserEntityDto user, String[] role);

    void resetPassword(String email) throws CustomRuntimeException;

    void deleteUser(long id) throws CustomRuntimeException;

    UserEntityDto register(UserEntityDto user) throws CustomRuntimeException;

    UserEntityDto toDto(UserEntity user);
}
