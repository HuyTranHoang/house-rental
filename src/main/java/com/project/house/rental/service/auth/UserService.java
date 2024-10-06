package com.project.house.rental.service.auth;

import com.project.house.rental.dto.auth.ChangePasswordDto;
import com.project.house.rental.dto.auth.ProfileDto;
import com.project.house.rental.dto.auth.UserEntityDto;
import com.project.house.rental.dto.params.UserParams;
import com.project.house.rental.entity.auth.UserEntity;
import com.project.house.rental.exception.CustomRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface UserService {

    Map<String, Object> getAllUserWithParams(UserParams userParams);

    UserEntityDto getUserById(long id);

    UserEntityDto addNewUser(UserEntityDto user) throws CustomRuntimeException;

    UserEntityDto updateUser(long id, UserEntityDto user) throws CustomRuntimeException;

    UserEntityDto lockUser(long id) throws CustomRuntimeException;

    void deleteUser(long id) throws CustomRuntimeException;

    UserEntityDto updateProfile(ProfileDto profileDto, HttpServletRequest request) throws CustomRuntimeException;

    UserEntityDto changePassword(ChangePasswordDto changePasswordDto, HttpServletRequest request) throws CustomRuntimeException;

    UserEntityDto updateAvatar(MultipartFile avatar, HttpServletRequest request) throws CustomRuntimeException, IOException;

    UserEntityDto updateRole(long id, List<String> roles) throws CustomRuntimeException;

    void deleteMultipleUsers(List<Long> ids);

    void updateBalance(long id, double amount) throws CustomRuntimeException;

    void online(UserEntity user) throws CustomRuntimeException;

    void offline(UserEntity user) throws CustomRuntimeException;
}
