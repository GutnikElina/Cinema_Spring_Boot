package org.cinema.handler;

import lombok.extern.slf4j.Slf4j;
import org.cinema.exception.EntityAlreadyExistException;
import org.cinema.exception.NoDataFoundException;
import org.cinema.exception.OmdbApiException;

@Slf4j
public class ErrorHandler {
    public static String resolveErrorMessage(Exception e) {
        if (e instanceof IllegalArgumentException) {
            return "Error! Invalid input: " + e.getMessage();
        } else if (e instanceof NoDataFoundException) {
            return "Error! No data found: " + e.getMessage();
        } else if (e instanceof EntityAlreadyExistException) {
            return e.getMessage();
        } else if (e instanceof OmdbApiException) {
            return "Error! Failed to communicate with OMDB API. Please try again later.";
        } else {
            return "An unexpected error occurred.";
        }
    }
}
