package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.model.ItemRequestDTO;
import ru.practicum.shareit.requests.model.RequestDTO;
import ru.practicum.shareit.requests.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    RequestDTO create(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                      @RequestBody RequestDTO itemRequestDto) {
        log.info(":::POST /requests userId={} создание запроса на предмет с описанием: {}", userId,
                itemRequestDto.getDescription());
        return itemRequestService.create(userId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    ItemRequestDTO read(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                        @PathVariable long requestId) {
        log.info(":::GET /requests/{} чтение запроса по айди", requestId);
        return itemRequestService.read(userId, requestId);
    }

    @GetMapping()
    List<ItemRequestDTO> getUsersRequests(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(":::GET /requests/ userId={} чтение запросов пользователя", userId);
        return itemRequestService.getUsersRequests(userId);
    }

    @GetMapping("/all")
    List<ItemRequestDTO> readAll(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                 @Valid @Positive @Nullable @RequestParam(defaultValue = "1") int from,
                                 @Valid @Nullable @RequestParam(defaultValue = "10") int size) {
        log.info(":::GET /requests/all userId={} чтение всех запросов пользователем. from={}, size={}",
                userId, from, size);
        return itemRequestService.readAll(userId, from, size);
    }
}
