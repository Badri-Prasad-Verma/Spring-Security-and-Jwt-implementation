package com.SpringAndJWtImplement.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class JWtResponse {
    private String token;
    private String refreshToken;
    private String username;

}
