package com.SpringAndJWtImplement.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tokenId;

    private String refreshToken;
    private Instant expiry;

    @OneToOne
    private User user;
}
