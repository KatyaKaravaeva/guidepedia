package com.guidepedia.service;

import com.guidepedia.exception.MyEntityNotFoundException;
import com.guidepedia.exception.TokenRefreshException;
import com.guidepedia.model.entity.RefreshToken;
import com.guidepedia.repo.RefreshRepository;
import com.guidepedia.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${jwt.refreshSessionTime}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;


    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        System.out.println("createrefresh");
        refreshToken.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new MyEntityNotFoundException(userId)));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs).toString());
        refreshToken.setToken(UUID.randomUUID().toString());
        System.out.println("do save");
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (Instant.parse(token.getExpiryDate()).compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}