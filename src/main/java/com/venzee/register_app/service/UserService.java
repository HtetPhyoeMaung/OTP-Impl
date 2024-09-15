package com.venzee.register_app.service;

import com.venzee.register_app.dto.request.RegisterRequest;
import com.venzee.register_app.dto.response.CommonResponse;
import com.venzee.register_app.dto.response.RegisterResponse;
import com.venzee.register_app.entity.User;
import com.venzee.register_app.exception.AlreadyExistsException;
import com.venzee.register_app.exception.NotFoundException;
import com.venzee.register_app.repository.UserRepository;
import com.venzee.register_app.utli.MailUtil;
import com.venzee.register_app.utli.OtpUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private OtpUtil otpUtil;
    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<RegisterResponse> register(RegisterRequest registerRequest) throws MessagingException {

        Optional<User> checkUser = userRepository.findByEmail(registerRequest.getEmail());
        if (checkUser.isPresent()){
            throw new AlreadyExistsException("Email is already exists!");
        }
        String otp = otpUtil.generateOtp();
        mailUtil.sendOtpEmail(registerRequest.getEmail(),otp);

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
        RegisterResponse response = new RegisterResponse();
        response.setStatus(HttpStatus.CREATED.value());
        response.setMessage("User registration success.");
        response.setTimeStamp(new Date());

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    public ResponseEntity<CommonResponse> verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email).orElseThrow(()->
                new NotFoundException("User not found by this email : "+email));

        if (user.getOtp().equals(otp)&& Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds()<(1*60)){
        user.setActive(true);
        userRepository.save(user);
        CommonResponse response = new CommonResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("OTP Verified");
        response.setTimeStamp(new Date());
        return new ResponseEntity<>(response,HttpStatus.OK);
        }
        CommonResponse response = new CommonResponse();
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        response.setMessage("OTP is invalid! Please resend it again");
        response.setTimeStamp(new Date());
        return new ResponseEntity<>(response,HttpStatus.NOT_ACCEPTABLE);

    }

    public ResponseEntity<CommonResponse> resendCode(String email) throws MessagingException {
        User user = userRepository.findByEmail(email).orElseThrow(()->
                new NotFoundException("User not found by this email : "+email));
        String otp = otpUtil.generateOtp();
        mailUtil.sendOtpEmail(email,otp);
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);

        CommonResponse response = new CommonResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Please resend it again");
        response.setTimeStamp(new Date());
        return new ResponseEntity<>(response,HttpStatus.OK);

    }
}
