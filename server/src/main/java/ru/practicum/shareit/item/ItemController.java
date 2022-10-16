package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.CommentDTO;
import ru.practicum.shareit.item.model.ItemDTO;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDTO create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody ItemDTO itemDto) {
        log.info(":::POST /items userId={}  itemDto={} создание прдемета", userId, itemDto);
        return itemService.create(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDTO read(@PathVariable long itemId,
                        @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info(":::GET /items/{} чтение предмета по айди", itemId);
        return itemService.read(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDTO update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long itemId,
                          @RequestBody ItemDTO itemDto) {
        log.info(":::PATCH /items/{} userId={}  body={} обновление предмета", itemId, userId, itemDto);
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping
    public List<ItemDTO> getOwnersItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @PositiveOrZero @Nullable @RequestParam(defaultValue = "0") int from,
                                        @Nullable @RequestParam(defaultValue = "10") int size) {
        log.info(":::GET /items/ userId={} чтение предметов собственника", userId);
        return itemService.getOwnersItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDTO> findItemsByText(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestParam String text,
                                         @PositiveOrZero @Nullable @RequestParam(defaultValue = "0") int from,
                                         @Nullable @RequestParam(defaultValue = "10") int size) {
        log.info(":::GET /search?text={} поиск по тексту", text);
        return itemService.findItemsByText(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    CommentDTO createComment(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                             @PathVariable long itemId,
                             @RequestBody @Valid CommentDTO commentDto) {
        log.info(":::POST /items/{}/comment создание коммента пользователем", itemId);
        return itemService.createComment(userId, itemId, commentDto);
    }
}
