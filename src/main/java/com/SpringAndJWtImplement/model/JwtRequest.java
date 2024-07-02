package com.SpringAndJWtImplement.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtRequest {
    private String email;
    private String password;


    @Override
    public String toString() {
        return "JwtRequest{" +
                ", password='" + password + '\'' +
                '}';
    }
}
