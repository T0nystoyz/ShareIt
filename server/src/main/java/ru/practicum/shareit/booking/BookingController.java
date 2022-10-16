package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.RequestBookingDTO;
import ru.practicum.shareit.booking.model.ResponseBookingDTO;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.constraints.Positive;
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
        log.info(":::POST /bookings userId={} создание аренды на предмет с id={}", userId,
                requestBookingDto.getItemId());
        return bookingService.create(userId, requestBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseBookingDTO approveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long bookingId,
                                             @RequestParam boolean approved) {
        log.info(":::PATCH /bookings подтвердить аренду по айди {} пользователем {}", bookingId, userId);
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseBookingDTO getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long bookingId) {
        log.info(":::GET /bookings просмотр аренды по айди {} пользователем userId={} ", bookingId, userId);
        return bookingService.read(userId, bookingId);
    }

    @GetMapping
    public List<ResponseBookingDTO> getBookingsByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestParam(required = false, defaultValue = "ALL") State state,
                                                      @Positive @Nullable @RequestParam(defaultValue = "1") int from,
                                                      @Nullable @RequestParam(defaultValue = "10") int size
    ) {
        log.info(":::GET /bookings userId={} просмотр всей аренды пользователем", userId);
        return bookingService.readBookingByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<ResponseBookingDTO> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                       @RequestParam(required = false,
                                                               defaultValue = "ALL") State state,
                                                       @Positive @Nullable @RequestParam(defaultValue = "1") int from,
                                                       @Nullable @RequestParam(defaultValue = "10") int size) {
        log.info(":::GET /bookings ownerId={} просмотр всей аренды собственником", ownerId);
        return bookingService.readBookingByOwner(ownerId, state, from, size);
    }
}