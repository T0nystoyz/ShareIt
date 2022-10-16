package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/requests")
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    public ItemRequestController(ItemRequestClient requestClient) {
        this.requestClient = requestClient;
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Valid @RequestBody ItemRequestDTO itemRequestDto) {
        log.info(":::POST /requests userId={} создание запроса на предмет с описанием: {}", userId,
                itemRequestDto.getDescription());
        return requestClient.create(userId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> read(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @PathVariable long requestId) {
        log.info(":::GET /requests/{} чтение запроса по айди", requestId);
        return requestClient.read(userId, requestId);
    }

    @GetMapping()
    public ResponseEntity<Object> getUsersRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(":::GET /requests/ userId={} чтение запросов пользователя", userId);
        return requestClient.getUsersRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> readAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @Valid @RequestParam(name = "from", defaultValue = "1") int from,
                                          @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info(":::GET /requests/all userId={} чтение всех запросов пользователем. from={}, size={}",
                userId, from, size);
        return requestClient.readAll(userId, from, size);
    }


}
