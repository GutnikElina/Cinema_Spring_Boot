package org.cinema.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cinema.dto.userDTO.UserCreateDTO;
import org.cinema.handler.ErrorHandler;
import org.cinema.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Admin User Management", description = "Endpoints for managing users by admins")
public class AdminUserController {

    private final UserService userService;

    @Operation(summary = "Get all users", description = "Retrieves a list of all users.")
    @GetMapping
    public ResponseEntity<?> getUsers() {
        log.debug("Fetching all users...");

        try {
            return ResponseEntity.ok(userService.findAll());
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique ID.")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {
        log.debug("Fetching user by id: {}...", userId);

        try {
            return ResponseEntity.ok(userService.getById(userId));
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(summary = "Add a new user", description = "Creates a new user with the provided details.")
    @PostMapping("/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserCreateDTO createDTO) {
        log.debug("Adding user...");

        try {
            return ResponseEntity.ok(userService.save(createDTO));
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(summary = "Edit an existing user", description = "Updates the details of an existing user.")
    @PutMapping("/edit/{userId}")
    public ResponseEntity<?> editUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId,
            @Valid @RequestBody UserCreateDTO updateDTO) {
        log.debug("Editing user...");

        try {
            return ResponseEntity.ok(userService.update(userId, updateDTO));
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by their unique ID.")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {
        log.debug("Deleting user...");

        try {
            return ResponseEntity.ok(userService.delete(userId));
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }
}
