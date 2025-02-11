package org.cinema.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cinema.dto.filmSessionDTO.FilmSessionResponseDTO;
import org.cinema.dto.ticketDTO.TicketCreateDTO;
import org.cinema.handler.ErrorHandler;
import org.cinema.service.SessionService;
import org.cinema.service.TicketService;
import org.cinema.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user/tickets/purchase")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Ticket Purchase", description = "Methods for purchasing and viewing tickets for users")
public class TicketPurchaseController {

    private final TicketService ticketService;
    private final SessionService sessionService;
    private final UserService userService;

    @Operation(
            summary = "Get film sessions",
            description = "Fetches a list of film sessions. If a date is provided, it filters the sessions by that date."
    )
    @GetMapping
    public ResponseEntity<?> getFilmSessions(
            @Parameter(
                    name = "date",
                    description = "The date to filter film sessions by (optional)",
                    example = "2025-02-15",
                    required = false
            )
            @RequestParam(required = false) String date) {

        log.debug("Fetching film sessions...");

        try {
            List<FilmSessionResponseDTO> filmSessions = StringUtils.isBlank(date)
                    ? sessionService.findAll()
                    : sessionService.findByDate(date);

            if (filmSessions.isEmpty() && StringUtils.isNotBlank(date)) {
                log.warn("No film sessions found for the selected date: {}. Returning all sessions.", date);
                filmSessions = sessionService.findAll();
            }
            return ResponseEntity.ok(filmSessions);
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(
            summary = "Get session details",
            description = "Fetches the details of a specific session, including ticket availability."
    )
    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getSessionDetails(
            @Parameter(
                    name = "sessionId",
                    description = "The ID of the session to fetch details for",
                    required = true
            )
            @PathVariable String sessionId) {

        log.debug("Fetching session details for sessionId: {}...", sessionId);

        try {
            FilmSessionResponseDTO selectedSession = ticketService.getSessionDetailsWithTickets(sessionId);
            return ResponseEntity.ok(selectedSession);
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }

    @Operation(
            summary = "Purchase a ticket",
            description = "Allows the user to purchase a ticket for a specific session."
    )
    @PostMapping
    public ResponseEntity<?> purchaseTicket(
            Authentication authentication,
            @Parameter(
                    name = "ticketCreateDTO",
                    description = "Ticket details to be used for the purchase"
            )
            @RequestBody TicketCreateDTO ticketCreateDTO) {

        String username = authentication.getName();
        log.debug("Processing ticket purchase for user: {}...", username);

        try {
            Long userId = userService.findByUsername(username).getId();
            ticketCreateDTO.setUserId(userId);
            String message = ticketService.purchaseTicket(ticketCreateDTO);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }
}