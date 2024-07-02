package com.SpringAndJWtImplement.service;

import com.SpringAndJWtImplement.entity.RefreshToken;
import com.SpringAndJWtImplement.entity.User;
import com.SpringAndJWtImplement.repository.RefreshTokenRepository;
import com.SpringAndJWtImplement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private long refreshToken=2*60*1000;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    public RefreshToken createToken(String username){
        User user = userRepository.findByEmail(username).get();
        RefreshToken refreshToken1 = user.getRefreshToken();
        if(refreshToken1==null){
            refreshToken1 = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expiry(Instant.now().plusMillis(refreshToken))
                    .user(user)
                    .build();
        }else {
            refreshToken1.setExpiry(Instant.now().plusMillis(refreshToken));
        }
        user.setRefreshToken(refreshToken1);
        RefreshToken save = refreshTokenRepository.save(refreshToken1);
        return refreshToken1;
    }

    public RefreshToken verifyToken(String refreshToken){
        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Token does not exists in database !!"));
        if(token.getExpiry().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh Token has expired !!");
        }

        return token;
    }

}
