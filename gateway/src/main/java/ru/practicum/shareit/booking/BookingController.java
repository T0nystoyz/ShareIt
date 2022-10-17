package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.RequestBookingDTO;
import ru.practicum.shareit.booking.dto.State;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookingsByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @RequestParam(
                                                            name = "state", defaultValue = "all") String stateParam,
                                                    @PositiveOrZero @RequestParam(
                                                            name = "from", defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(
                                                            name = "size", defaultValue = "10") Integer size) {
        State state = State.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info(":::GET /bookings userId={} просмотр всей аренды пользователем", userId);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                     @RequestParam(
                                                             name = "state", defaultValue = "all") String stateParam,
                                                     @PositiveOrZero @RequestParam(
                                                             name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(
                                                             name = "size", defaultValue = "10") Integer size) {
        State state = State.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info(":::GET /bookings ownerId={} просмотр всей аренды собственником", ownerId);
        return bookingClient.getBookingCurrentOwner(ownerId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Valid RequestBookingDTO requestDto) {
        validateTime(requestDto);

        log.info(":::POST /bookings userId={} создание аренды на предмет с id={}", userId,
                requestDto.getItemId());
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info(":::GET /bookings просмотр аренды по айди {} пользователем userId={} ", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }


    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable Long bookingId,
                                                 @RequestParam boolean approved) {
        log.info(":::PATCH /bookings подтвердить аренду по айди {} пользователем {}", bookingId, userId);
        return bookingClient.approveStatus(userId, bookingId, approved);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> errorHandler(IllegalArgumentException ex) {
        Map<String, String> resp = new HashMap<>();
        resp.put("error", "Unknown state: UNSUPPORTED_STATUS");
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<String> creatingWithWrongTime(ValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private void validateTime(RequestBookingDTO requestBookingDto) {
        if (requestBookingDto.getStart().isAfter(requestBookingDto.getEnd())) {
            throw new ValidationException("начало не может быть позже окончания аренды");
        }
        log.info("валидация времени аренды прошла успешно: начало {} окончание {}", requestBookingDto.getStart(),
                requestBookingDto.getEnd());
    }
}