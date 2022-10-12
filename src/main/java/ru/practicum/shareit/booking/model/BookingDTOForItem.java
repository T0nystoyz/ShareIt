package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDTOForItem {
    private long id;
    private long bookerId;
    private LocalDateTime end;
    private LocalDateTime start;


}
