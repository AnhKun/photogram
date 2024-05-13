package com.anguyen.photogram.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tokenId;
    private String refreshTokenString;
    private Instant expirationTime;
    @OneToOne
    private UserEntity user;
}
