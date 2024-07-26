package com.project.house.rental.service.auth;

import com.project.house.rental.dto.auth.ChangePasswordDto;
import com.project.house.rental.dto.auth.ProfileDto;
import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.exception.CustomRuntimeException;
import jakarta.servlet.http.HttpServletRequest;


public interface UserService {
    UserEntityDto addNewUser(UserEntityDto user, String[] role);

    UserEntityDto updateUser(UserEntityDto user, String[] role);

    void resetPassword(String email) throws CustomRuntimeException;

    void deleteUser(long id) throws CustomRuntimeException;

    UserEntityDto updateProfile(ProfileDto profileDto, HttpServletRequest request) throws CustomRuntimeException;

    UserEntityDto changePassword(ChangePasswordDto changePasswordDto, HttpServletRequest request) throws CustomRuntimeException;

    UserEntityDto register(UserEntityDto user) throws CustomRuntimeException;

    UserEntityDto toDto(UserEntity user);

}
