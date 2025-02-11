package org.cinema.security;

import lombok.RequiredArgsConstructor;
import org.cinema.model.RefreshToken;
import org.cinema.repository.RefreshTokenRepository;
import org.cinema.model.User;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void createRefreshToken(User user, String token, Instant expiryDate) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiresAt(expiryDate);
        refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public RefreshToken findValidTokenByUser(User user) {
        Optional<RefreshToken> storedToken = refreshTokenRepository.findByUser(user);

        if (storedToken.isPresent()) {
            RefreshToken token = storedToken.get();
            if (token.getExpiresAt().isAfter(Instant.now())) {
                return token;
            } else {
                refreshTokenRepository.delete(token);
            }
        }
        return null;
    }
}