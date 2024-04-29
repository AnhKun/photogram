package com.anguyen.photogram.util;

import com.anguyen.photogram.security.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class JwtSecurityUtil {
    public static Optional<UserDetailsImpl> getJwtUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getDetails() != null) {
            Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return object instanceof UserDetailsImpl ? Optional.of((UserDetailsImpl) object) : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    private JwtSecurityUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
