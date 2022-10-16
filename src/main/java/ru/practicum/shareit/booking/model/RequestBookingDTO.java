package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBookingDTO {
    private Long id;
    @Future
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
    private Long itemId;
    private Long booker;
    private Status status;
}
