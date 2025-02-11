package org.cinema.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cinema.dto.ticketDTO.TicketCreateDTO;
import org.cinema.dto.ticketDTO.TicketUpdateDTO;
import org.cinema.handler.ErrorHandler;
import org.cinema.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/ticket-management")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Admin Ticket Management", description = "Endpoints for managing tickets by admins")
public class AdminTicketController {

    private final TicketService ticketService;

    @Operation(summary = "Get all tickets", description = "Retrieves a list of all available tickets.")
    @GetMapping
    public ResponseEntity<?> getTickets() {
        log.debug("Fetching all tickets...");

        try {
            return ResponseEntity.ok(ticketService.findAll());
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(summary = "Get ticket by ID", description = "Retrieves a ticket by its unique ID.")
    @GetMapping("/{ticketId}")
    public ResponseEntity<?> getTicketById(
            @Parameter(description = "Ticket ID", required = true)
            @PathVariable String ticketId) {
        log.debug("Fetching ticket by id: {}...", ticketId);

        try {
            return ResponseEntity.ok(ticketService.getById(ticketId));
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(summary = "Add a new ticket", description = "Creates a new ticket with the provided details.")
    @PostMapping("/add")
    public ResponseEntity<?> addTicket(
            @Valid @RequestBody TicketCreateDTO createDTO) {
        log.debug("Adding ticket...");

        try {
            return ResponseEntity.ok(ticketService.save(createDTO));
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(summary = "Edit an existing ticket", description = "Updates the details of an existing ticket.")
    @PutMapping("/edit")
    public ResponseEntity<?> editTicket(
            @Valid @RequestBody TicketUpdateDTO updateDTO) {
        log.debug("Editing ticket...");

        try {
            return ResponseEntity.ok(ticketService.update(updateDTO));
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(summary = "Delete a ticket", description = "Deletes a ticket by its unique ID.")
    @DeleteMapping("/delete/{ticketId}")
    public ResponseEntity<?> deleteTicket(
            @Parameter(description = "Ticket ID", required = true)
            @PathVariable String ticketId) {
        log.debug("Deleting ticket...");

        try {
            return ResponseEntity.ok(ticketService.delete(ticketId));
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }
}