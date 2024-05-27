package com.anguyen.photogram.repositories;

import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.anguyen.photogram.entities.ForgotPassword;
import com.anguyen.photogram.entities.UserEntity;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

    @Query("SELECT fp FROM ForgotPassword fp WHERE fp.otp = ?1 AND fp.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, UserEntity user);

    @Modifying
    @Transactional
    void deleteByUser(UserEntity user);
}
