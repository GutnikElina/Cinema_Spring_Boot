package org.cinema.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cinema.dto.filmSessionDTO.FilmSessionCreateDTO;
import org.cinema.dto.filmSessionDTO.FilmSessionUpdateDTO;
import org.cinema.handler.ErrorHandler;
import org.cinema.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/admin/sessions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Admin Film Sessions", description = "Endpoints for managing film sessions by admins")
public class AdminSessionController {

    private final SessionService sessionService;

    @Operation(summary = "Get all film sessions", description = "Retrieves a list of all available film sessions.")
    @GetMapping
    public ResponseEntity<?> getSessions() {
        log.debug("Fetching all sessions...");

        try {
            return ResponseEntity.ok(sessionService.findAll());
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(summary = "Get a film session by ID", description = "Retrieves a film session by its unique ID.")
    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getSessionById(
            @Parameter(description = "Session ID", required = true)
            @PathVariable String sessionId) {
        log.debug("Fetching session by id: {}...", sessionId);

        try {
            return ResponseEntity.ok(sessionService.getById(sessionId));
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(summary = "Add a new film session", description = "Creates a new film session with the provided details.")
    @PostMapping("/add")
    public ResponseEntity<?> addSession(
            @Valid @RequestBody FilmSessionCreateDTO createDTO) {
        log.debug("Adding film session ...");

        try {
            return ResponseEntity.ok(sessionService.save(createDTO, createDTO.getMovieId()));
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(summary = "Edit an existing film session", description = "Updates the details of an existing film session.")
    @PutMapping("/edit")
    public ResponseEntity<?> editSession(
            @Valid @RequestBody FilmSessionUpdateDTO updateDTO) {
        log.debug("Editing film session ...");

        try {
            return ResponseEntity.ok(sessionService.update(updateDTO, updateDTO.getMovieId()));
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(summary = "Delete a film session", description = "Deletes a film session by its unique ID.")
    @DeleteMapping("/delete/{sessionId}")
    public ResponseEntity<?> deleteSession(
            @Parameter(description = "Session ID", required = true)
            @PathVariable String sessionId) {
        log.debug("Deleting film session ...");

        try {
            return ResponseEntity.ok(sessionService.delete(sessionId));
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }
}