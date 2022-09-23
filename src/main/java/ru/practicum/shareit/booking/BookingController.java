package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.RequestBookingDTO;
import ru.practicum.shareit.booking.model.ResponseBookingDTO;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseBookingDTO create(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestBody RequestBookingDTO requestBookingDto) {
        log.info(":::POST userId={} создание аренды", userId);
        return bookingService.create(userId, requestBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseBookingDTO approveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long bookingId,
                                             @RequestParam boolean approved) {
        log.info(":::PATCH подтвердить аренду по айди {} пользователем {}", bookingId, userId);
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseBookingDTO getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long bookingId) {
        log.info(":::GET userId={} просмотр аренды по айди {} пользователем", userId, bookingId);
        return bookingService.read(userId, bookingId);
    }

    @GetMapping
    public List<ResponseBookingDTO> getBookingsByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestParam(required = false, defaultValue = "ALL") State state) {
        log.info(":::GET userId={} просмотр всей аренды пользователем", userId);
        return bookingService.readBookingByUser(userId, state);
    }

    @GetMapping("/owner")
    public List<ResponseBookingDTO> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                       @RequestParam(required = false, defaultValue = "ALL") State state) {
        log.info(":::GET ownerId={} просмотр всей аренды собственником", ownerId);
        return bookingService.readBookingByOwner(ownerId, state);
    }
}