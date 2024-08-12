package com.project.house.rental.service.auth;

import com.project.house.rental.dto.auth.ChangePasswordDto;
import com.project.house.rental.dto.auth.ProfileDto;
import com.project.house.rental.dto.auth.ResetPasswordDto;
import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.exception.CustomRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface UserService {
    UserEntityDto addNewUser(UserEntityDto user) throws CustomRuntimeException;

    UserEntityDto updateUser(long id, UserEntityDto user) throws CustomRuntimeException;

    void sendEmailResetPassword(String email) throws CustomRuntimeException;

    UserEntityDto resetPassword(ResetPasswordDto resetPasswordDto) throws CustomRuntimeException;

    void deleteUser(long id) throws CustomRuntimeException;

    UserEntityDto updateProfile(ProfileDto profileDto, HttpServletRequest request) throws CustomRuntimeException;

    UserEntityDto changePassword(ChangePasswordDto changePasswordDto, HttpServletRequest request) throws CustomRuntimeException;

    UserEntityDto updateAvatar(MultipartFile avatar, HttpServletRequest request) throws CustomRuntimeException, IOException;

    UserEntityDto register(UserEntityDto user) throws CustomRuntimeException;

    UserEntityDto toDto(UserEntity user);

}
