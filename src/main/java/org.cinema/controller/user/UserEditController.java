package org.cinema.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cinema.dto.userDTO.UserResponseDTO;
import org.cinema.dto.userDTO.UserUpdateDTO;
import org.cinema.handler.ErrorHandler;
import org.cinema.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "User Profile", description = "Methods for working with the user's profile")
public class UserEditController {

    private final UserService userService;

    @Operation(
            summary = "Get the user's profile",
            description = "Returns information about the current user"
    )
    @GetMapping
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String username = authentication.getName();
        log.debug("Fetching user profile for username: {}", username);

        try {
            UserResponseDTO user = userService.findByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(
            summary = "Update the user's profile",
            description = "Allows updating the current user's information"
    )
    @PutMapping
    public ResponseEntity<?> updateProfile(
            Authentication authentication,
            @Parameter(
                    name = "userUpdateDTO",
                    description = "Data for updating the user's profile"
            )
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        String username = authentication.getName();
        log.debug("Updating profile for username: {}", username);

        try {
            UserResponseDTO user = userService.findByUsername(username);
            userService.updateProfile(user.getId(), userUpdateDTO);
            return ResponseEntity.ok("Profile updated successfully.");
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }
}