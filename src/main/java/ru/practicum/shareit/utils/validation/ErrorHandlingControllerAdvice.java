package ru.practicum.shareit.utils.validation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.utils.exceptions.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(violation -> new Violation(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleWrongId(IndexOutOfBoundsException e) {
        return Map.of("error", "по такому айди данных нет " + e.getCause());
    }

    @ExceptionHandler(UniqueEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseEntity<String> onIdValidationIdException(UniqueEmailException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<String> userNotFound(UserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<String> onIdValidationIdException(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> creatingWithNoEmail(ValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> tryingToBook(ItemNotAvailableException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<String> tryingToBook(ItemNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWrongStatus(IllegalArgumentException e) {
        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS", e.getMessage());
    }

    @ExceptionHandler(BookingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<String> handleWrongBookingId(BookingNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ApproveNotWaitingBookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> handleApprovedStatus(ApproveNotWaitingBookingException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(OwnerBookingOwnItemException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<String> handleBookingByOwner(OwnerBookingOwnItemException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(EmptyCommentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> handleEmptyComment(EmptyCommentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserIsNotOwnerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<String> userHasNothing(UserIsNotOwnerException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
