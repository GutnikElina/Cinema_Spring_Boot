package org.cinema.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cinema.service.TicketService;
import org.cinema.handler.ErrorHandler;
import org.cinema.constants.ParamConstant;
import org.cinema.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user/tickets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "User Tickets", description = "Methods for managing user tickets")
public class MyTicketsController {

    private final TicketService ticketService;
    private final UserService userService;

    @Operation(
            summary = "Get tickets for the user",
            description = "Fetches the list of tickets associated with the currently authenticated user."
    )
    @GetMapping
    public ResponseEntity<?> getUserTickets(Authentication authentication) {
        String username = authentication.getName();
        log.debug("Fetching tickets for user: {}", username);

        try {
            Long userId = userService.findByUsername(username).getId();
            return ResponseEntity.ok(ticketService.findByUserId(userId.toString()));
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(
            summary = "Return a ticket",
            description = "Handles the process of returning a ticket using its ID."
    )
    @PostMapping("/return")
    public ResponseEntity<?> returnTicket(
            @Parameter(
                    name = "ticketId",
                    description = "The ID of the ticket to return",
                    required = true
            )
            @RequestParam(required = false) Long ticketId) {
        log.debug("Processing ticket return for ticket ID: {}", ticketId);

        if (ticketId == null) {
            return ResponseEntity.badRequest().body("Error! No ticket ID provided.");
        }

        try {
            String message = ticketService.processTicketAction(ParamConstant.RETURN_TICKETS_PARAM, ticketId);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }
}
