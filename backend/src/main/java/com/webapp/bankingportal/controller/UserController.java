package com.webapp.bankingportal.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.bankingportal.dto.LoginRequest;
import com.webapp.bankingportal.dto.OtpRequest;
import com.webapp.bankingportal.dto.OtpVerificationRequest;
import com.webapp.bankingportal.entity.User;
import com.webapp.bankingportal.exception.InvalidTokenException;
import com.webapp.bankingportal.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user, @org.springframework.web.bind.annotation.RequestParam String otp) {
        log.info("Received request to register user with email: {}", user.getEmail());
        return userService.registerUser(user, otp);
    }

    @PostMapping("/register/otp")
    public ResponseEntity<String> generateRegistrationOtp(@RequestBody java.util.Map<String, String> payload) {
        String email = payload.get("email");
        String name = payload.get("name");
        log.info("Received request to generate registration OTP for email: {}", email);
        return userService.generateRegistrationOtp(email, name);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request)
            throws InvalidTokenException {
        log.info("Received login request for identifier: {}", loginRequest.identifier());
        return userService.login(loginRequest, request);
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<String> generateOtp(@Valid @RequestBody OtpRequest otpRequest) {
        log.info("Received request to generate OTP for identifier: {}", otpRequest.identifier());
        return userService.generateOtp(otpRequest);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtpAndLogin(@Valid @RequestBody OtpVerificationRequest otpVerificationRequest)
            throws InvalidTokenException {
        log.info("Received request to verify OTP for identifier: {}", otpVerificationRequest.identifier());
        return userService.verifyOtpAndLogin(otpVerificationRequest);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@Valid @RequestBody User user) {
        log.info("Received request to update user profile");
        return userService.updateUser(user);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody java.util.Map<String, String> payload, 
            @RequestHeader("Authorization") String token) {
        log.info("Received request to change password");
        String oldPassword = payload.get("oldPassword");
        String newPassword = payload.get("newPassword");
        return userService.changePassword(oldPassword, newPassword, token);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token)
            throws InvalidTokenException {
        log.info("Received request to logout");
        return userService.logout(token);
    }

}
