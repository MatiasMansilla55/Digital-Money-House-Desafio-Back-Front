package com.example.demo.service;

import com.example.demo.dto.entry.AuthResponse;
import com.example.demo.dto.entry.LoginRequest;
import com.example.demo.dto.entry.UserEntryDto;
import com.example.demo.dto.exit.LoginResponse;
import com.example.demo.dto.exit.UserRegisterOutDto;
import com.example.demo.entity.User;
import com.example.demo.exceptions.EmailNotVerifiedException;
import com.example.demo.exceptions.IncorrectPasswordException;
import com.example.demo.exceptions.UserNotFoundException;
import lombok.NonNull;


import java.io.IOException;

import java.util.Map;

public interface IUserService {

    User createUser(@NonNull UserEntryDto userEntryDto);
    void deleteUser(Long userId);
    void updateUser(Long userId, UserEntryDto userEntryDto);

    void logoutUser(String token);
    String authenticateAndLogin(LoginRequest request) throws UserNotFoundException, IncorrectPasswordException, EmailNotVerifiedException;
    void verifyUserEmail(String email, String verificationCode);

    UserRegisterOutDto getUserById(Long id);
    Map<String, Object> handleUserRegistration(UserEntryDto userEntryDto) throws IOException;
    void processPasswordResetRequest(String email);
    void resetPassword(String token, String newPassword, String confirmPassword);
    //void updateAlias(Long id, String alias);
}
