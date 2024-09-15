package com.venzee.register_app.controller;

import com.venzee.register_app.dto.request.RegisterRequest;
import com.venzee.register_app.dto.response.CommonResponse;
import com.venzee.register_app.dto.response.RegisterResponse;
import com.venzee.register_app.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) throws MessagingException {
    return userService.register(registerRequest);
    }

    @PutMapping("/verify-account")
    public ResponseEntity<CommonResponse> verifyAccount(@RequestParam String email,@RequestParam String otp){
        return userService.verifyAccount(email,otp);
    }

    @PutMapping("/resend-code")
    public ResponseEntity<CommonResponse> resendCode(@RequestParam String email) throws MessagingException {
        return userService.resendCode(email);
    }
}
