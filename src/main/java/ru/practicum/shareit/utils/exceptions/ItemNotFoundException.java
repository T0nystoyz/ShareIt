package ru.practicum.shareit.utils.exceptions;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
