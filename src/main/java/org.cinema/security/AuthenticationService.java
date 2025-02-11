package org.cinema.security;

import lombok.RequiredArgsConstructor;
import org.cinema.dto.userDTO.UserCreateDTO;
import org.cinema.dto.userDTO.UserResponseDTO;
import org.cinema.dto.userDTO.UserUpdateDTO;
import org.cinema.mapper.userMapper.UserResponseMapper;
import org.cinema.model.RefreshToken;
import org.cinema.model.User;
import org.cinema.service.CustomUserDetailsService;
import org.cinema.service.UserService;
import org.cinema.util.ValidationUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CustomUserDetailsService customUserService;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public JwtAuthenticationResponse signUp(UserCreateDTO request) {
        userService.save(request);
        var userDetails = customUserService.loadUserByUsername(request.getUsername());
        var jwt = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);

        UserResponseDTO userResponseDTO = userService.findByUsername(request.getUsername());
        User user = UserResponseMapper.INSTANCE.toEntity(userResponseDTO);

        refreshTokenService.createRefreshToken(user, refreshToken, Instant.now().plusSeconds(60 * 60 * 24 * 7));

        return new JwtAuthenticationResponse(jwt, refreshToken);
    }

    public JwtAuthenticationResponse signIn(UserUpdateDTO request) {
        ValidationUtil.validateUser(request.getUsername(), request.getPassword());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var userDetails = customUserService.loadUserByUsername(request.getUsername());
        var jwt = jwtService.generateToken(userDetails);

        UserResponseDTO userResponseDTO = userService.findByUsername(request.getUsername());
        User user = UserResponseMapper.INSTANCE.toEntity(userResponseDTO);

        RefreshToken existingToken = refreshTokenService.findValidTokenByUser(user);
        String refreshToken;

        if (existingToken != null) {
            refreshToken = existingToken.getToken();
        } else {
            refreshToken = jwtService.generateRefreshToken(userDetails);
            refreshTokenService.createRefreshToken(user, refreshToken, Instant.now().plusSeconds(60 * 60 * 24 * 7));
        }

        return new JwtAuthenticationResponse(jwt, refreshToken);
    }

    public JwtAuthenticationResponse refreshToken(String refreshToken) {
        var storedToken = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (storedToken.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenService.deleteByToken(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        var username = jwtService.extractUserName(refreshToken);
        var userDetails = customUserService.loadUserByUsername(username);

        if (jwtService.isTokenValid(refreshToken, userDetails)) {
            var newAccessToken = jwtService.generateToken(userDetails);
            return new JwtAuthenticationResponse(newAccessToken, refreshToken);
        }
        throw new RuntimeException("Invalid refresh token");
    }
}
