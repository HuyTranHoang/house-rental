package com.project.house.rental.service.auth;

import com.project.house.rental.dto.auth.ResetPasswordDto;
import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.exception.CustomRuntimeException;

public interface AuthService {
    UserEntityDto register(UserEntityDto user) throws CustomRuntimeException;

    void sendEmailResetPassword(String email) throws CustomRuntimeException;

    UserEntityDto resetPassword(ResetPasswordDto resetPasswordDto) throws CustomRuntimeException;
}
