package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.ItemDTO;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDTO create(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDTO itemDto) {
        log.info("POST /items userId={}  itemDto={}", userId, itemDto);
        return itemService.create(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDTO read(@PathVariable long itemId) {
        log.info("GET /items/{}", itemId);
        return itemService.read(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDTO update(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                          @RequestBody ItemDTO itemDto) {
        log.info("PATCH /items/{} userId={}  body={}", itemId, userId, itemDto);
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping
    public List<ItemDTO> getOwnersItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET /items/ userId={}", userId);
        return itemService.getOwnersItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDTO> findItemsByText(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text) {
        log.info("GET /search?text={}", text);
        return itemService.findItemsByText(userId, text);
    }
}
