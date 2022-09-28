package ru.practicum.shareit.utils.exceptions;

public class UserIsNotOwnerException extends RuntimeException {
    public UserIsNotOwnerException(String message) {
        super(message);
    }
}
