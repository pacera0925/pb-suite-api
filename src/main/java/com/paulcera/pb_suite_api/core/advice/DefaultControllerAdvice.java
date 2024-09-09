package com.paulcera.pb_suite_api.core.advice;

import com.paulcera.pb_suite_api.core.dto.ResponseMessage;
import com.paulcera.pb_suite_api.core.exception.UserNotFoundException;
import com.paulcera.pb_suite_api.security.exception.AlreadyLoggedInException;
import com.paulcera.pb_suite_api.security.exception.InvalidRefreshTokenException;
import com.paulcera.pb_suite_api.security.exception.TokenNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultControllerAdvice {

    @ExceptionHandler({BadCredentialsException.class, AccessDeniedException.class})
    public ResponseEntity<ResponseMessage> unauthorizedExceptionHandler(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage(ex.getMessage()));
    }

    @ExceptionHandler(AlreadyLoggedInException.class)
    public ResponseEntity<ResponseMessage> conflictExceptionHandler(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage(ex.getMessage()));
    }

    @ExceptionHandler({TokenNotFoundException.class, InvalidRefreshTokenException.class, UserNotFoundException.class})
    public ResponseEntity<ResponseMessage> badRequestExceptionHandler(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage> internalServerErrorExceptionHandler(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("An unexpected error occurred."));
    }

}
