package ru.practicum.shareit.booking.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDTOForItem {
    private long id;
    private long bookerId;
    private LocalDateTime end;
    private LocalDateTime start;
}
