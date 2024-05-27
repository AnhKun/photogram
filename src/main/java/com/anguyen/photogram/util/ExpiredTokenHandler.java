package com.anguyen.photogram.util;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.anguyen.photogram.repositories.InvalidatedTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExpiredTokenHandler {
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at 12:00 AM
    private void deleteExpiredToken() {
        log.info("delete expired tokens");

        Date now = new Date();
        invalidatedTokenRepository.deleteByExpiryTimeBefore(now);
    }
}
