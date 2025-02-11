package org.cinema.controller.general;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.cinema.constants.ParamConstant;
import org.cinema.handler.ErrorHandler;
import org.cinema.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Movies", description = "Methods for searching and retrieving information about movies")
public class MovieController {

    private final MovieService movieService;

    @Operation(
            summary = "Get list of movies",
            description = "Returns a list of movies based on the title. If no title is provided, returns an empty list."
    )
    @GetMapping
    public ResponseEntity<?> getMovies(
            @Parameter(
                    name = "movieTitle",
                    description = "The title of the movie to search for",
                    example = "Inception",
                    required = false
            )
            @RequestParam(value = ParamConstant.MOVIE_TITLE_PARAM, required = false) String movieTitle) {

        log.debug("Fetching movies with title: {}...", movieTitle);

        try {
            if (StringUtils.isBlank(movieTitle)) {
                log.warn("No movie title provided. Returning empty list.");
                return ResponseEntity.ok(Collections.emptyList());
            }
            return ResponseEntity.ok(movieService.searchMovies(StringUtils.trim(movieTitle)));
        } catch (Exception e) {
            String errorMessage = ErrorHandler.resolveErrorMessage(e);
            log.error(errorMessage);
            return ResponseEntity.internalServerError().body(errorMessage);
        }
    }
}
