package com.anguyen.photogram.entities;

import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;

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
