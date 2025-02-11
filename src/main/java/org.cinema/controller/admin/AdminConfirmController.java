package org.cinema.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cinema.constants.ParamConstant;
import org.cinema.handler.ErrorHandler;
import org.cinema.service.TicketService;
import org.cinema.util.ValidationUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/confirm-tickets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Admin Ticket Confirmation", description = "Endpoints for managing ticket confirmations by admins")
public class AdminConfirmController {

    private final TicketService ticketService;

    @Operation(summary = "Get all tickets", description = "Retrieves a list of all tickets.")
    @GetMapping
    public ResponseEntity<?> getAllTickets() {
        log.debug("Fetching all tickets...");

        try {
            return ResponseEntity.ok(ticketService.findAll());
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(summary = "Process ticket action", description = "Processes an action on a specific ticket (e.g., approve, reject).")
    @PostMapping("/{action}")
    public ResponseEntity<?> handleTicketAction(
            @Parameter(description = "Ticket ID", required = true)
            @RequestParam(ParamConstant.ID_PARAM) String ticketId,

            @Parameter(description = "Action to perform (approve/reject)", required = true)
            @PathVariable String action) {
        log.debug("Processing action '{}' for ticket ID '{}'...", action, ticketId);

        if (ticketId == null) {
            return ResponseEntity.badRequest().body("Error! No ticket ID provided.");
        }

        try {
            ValidationUtil.validateParameters(action, ticketId);
            String message = ticketService.processTicketAction(action, ValidationUtil.parseLong(ticketId));
            return ResponseEntity.ok().body(message);
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }
}
