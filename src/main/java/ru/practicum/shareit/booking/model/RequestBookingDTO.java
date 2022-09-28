package ru.practicum.shareit.booking.model;

import lombok.Data;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
public class RequestBookingDTO {
    private Long id;
    @Future
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
    private long itemId;
    private long booker;
    private Status status;
}
