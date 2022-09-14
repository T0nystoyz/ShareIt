package ru.practicum.shareit.utils;

public class IdGenerator {
    private static long userId = 0;
    private static long itemId = 0;

    public static long nextUserId() {
        return ++userId;
    }

    public static long nextItemId() {
        return ++itemId;
    }

}
