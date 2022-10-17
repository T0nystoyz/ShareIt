package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.RequestBookingDTO;
import ru.practicum.shareit.booking.model.ResponseBookingDTO;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    ResponseBookingDTO create(long userId, RequestBookingDTO requestBookingDto);

    ResponseBookingDTO approveBooking(long userId, long bookingId, boolean approved);

    ResponseBookingDTO read(long userId, long bookingId);

    List<ResponseBookingDTO> readBookingByUser(long userId, State state, int from, int size);

    List<ResponseBookingDTO> readBookingByOwner(long userId, State state, int from, int size);
}
