package com.anguyen.photogram.service.impl;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anguyen.photogram.dto.request.ChangePassword;
import com.anguyen.photogram.dto.request.MailBody;
import com.anguyen.photogram.dto.request.VerifyEmailRequest;
import com.anguyen.photogram.dto.request.VerifyOtpRequest;
import com.anguyen.photogram.entities.ForgotPassword;
import com.anguyen.photogram.entities.UserEntity;
import com.anguyen.photogram.exceptions.ApiException;
import com.anguyen.photogram.exceptions.ErrorCode;
import com.anguyen.photogram.repositories.ForgotPasswordRepository;
import com.anguyen.photogram.repositories.UserRepository;
import com.anguyen.photogram.service.EmailService;
import com.anguyen.photogram.service.ForgotPasswordService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private final UserRepository userRepository;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private Random random = new Random();

    @Override
    public String verifyEmail(VerifyEmailRequest emailRequest) {
        UserEntity user = userRepository
                .findByEmailAndStatusTrue(emailRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email!"));

        forgotPasswordRepository.deleteByUser(user);

        int otp = random.nextInt(100_000, 999_999);
        MailBody mailBody = MailBody.builder()
                .to(emailRequest.getEmail())
                .subject("OTP for Forgot Password request")
                .text("This is the otp for your Forgot Password request: " + otp)
                .build();

        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 2 * 60 * 1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);

        return "Email sent for verification!";
    }

    @Override
    public String verifyOtp(VerifyOtpRequest otpRequest) {
        UserEntity user = userRepository
                .findByEmailAndStatusTrue(otpRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email!"));

        ForgotPassword fp = forgotPasswordRepository
                .findByOtpAndUser(otpRequest.getOtp(), user)
                .orElseThrow(() ->
                        new ApiException(ErrorCode.BAD_REQUEST, "Invalid OTP for email: " + otpRequest.getEmail()));

        if (Boolean.TRUE.equals(fp.getExpirationTime().before(Date.from(Instant.now())))) {
            forgotPasswordRepository.deleteById(fp.getFpid());
            return "OTP has expired!";
        }

        return "OTP verified!";
    }

    @Override
    public String changePassword(ChangePassword changePassword, String email) {
        if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
            return "Please enter the password again";
        }

        userRepository.updatePassword(email, passwordEncoder.encode(changePassword.password()));

        return "Password has been changed!";
    }
}
