package org.cinema.controller.general;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cinema.dto.userDTO.UserCreateDTO;
import org.cinema.dto.userDTO.UserUpdateDTO;
import org.cinema.handler.ErrorHandler;
import org.cinema.security.AuthenticationService;
import org.cinema.security.JwtAuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user", description = "Handles user registration by creating a new user account.")
    @PostMapping("/registration")
    public ResponseEntity<?> signUp(
            @Parameter(description = "User details for registration", required = true)
            @RequestBody UserCreateDTO userCreateDTO) {
        log.debug("Processing user registration for: {}...", userCreateDTO.getUsername());

        try {
            JwtAuthenticationResponse response = authenticationService.signUp(userCreateDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(summary = "Login user", description = "Handles user login and returns a JWT token if successful.")
    @PostMapping("/login")
    public ResponseEntity<?> signIn(
            @Parameter(description = "User credentials for login", required = true)
            @RequestBody UserUpdateDTO userUpdateDTO) {
        log.debug("Processing user login for: {}...", userUpdateDTO.getUsername());

        try {
            JwtAuthenticationResponse response = authenticationService.signIn(userUpdateDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(summary = "Refresh token", description = "Refreshes the authentication token using the provided refresh token.")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @Parameter(description = "Refresh token to get a new access token", required = true)
            @RequestBody String refreshToken) {
        log.debug("Refreshing token...");

        try {
            JwtAuthenticationResponse response = authenticationService.refreshToken(refreshToken);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }
}
