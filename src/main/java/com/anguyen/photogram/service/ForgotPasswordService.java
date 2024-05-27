package com.anguyen.photogram.service;

import com.anguyen.photogram.dto.request.ChangePassword;
import com.anguyen.photogram.dto.request.VerifyEmailRequest;
import com.anguyen.photogram.dto.request.VerifyOtpRequest;

public interface ForgotPasswordService {
    String verifyEmail(VerifyEmailRequest emailRequest);

    String verifyOtp(VerifyOtpRequest otpRequest);

    String changePassword(ChangePassword changePassword, String email);
}
