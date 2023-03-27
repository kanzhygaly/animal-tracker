package kz.yerakh.animaltrackerservice.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String KEY_ERRORS = "errors";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        var body = new HashMap<>();
        body.put(KEY_ERRORS, errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationException(ConstraintViolationException ex, WebRequest request) {
        var errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        var body = new HashMap<>();
        body.put(KEY_ERRORS, errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EntryAlreadyExistException.class, DuplicateItemException.class})
    public ResponseEntity<Object> handleConflict(EntryAlreadyExistException ex, WebRequest request) {
        var body = new HashMap<>();
        body.put(KEY_ERRORS, Collections.singletonList(ex.getMessage()));

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EntryNotFoundException ex, WebRequest request) {
        var body = new HashMap<>();
        body.put(KEY_ERRORS, Collections.singletonList(ex.getMessage()));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<Object> handleBadRequest(InvalidValueException ex, WebRequest request) {
        var body = new HashMap<>();
        body.put(KEY_ERRORS, Collections.singletonList(ex.getMessage()));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorisedException.class)
    public ResponseEntity<Object> handleForbidden(UnauthorisedException ex, WebRequest request) {
        var body = new HashMap<>();
        body.put(KEY_ERRORS, Collections.singletonList(ex.getMessage()));

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
}
