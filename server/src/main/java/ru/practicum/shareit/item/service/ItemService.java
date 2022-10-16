package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.CommentDTO;
import ru.practicum.shareit.item.model.ItemDTO;

import javax.transaction.Transactional;
import java.util.List;

public interface ItemService {

    List<ItemDTO> findItemsByText(long userId, String text, int from, int size);

    ItemDTO create(long userId, ItemDTO itemDto);

    ItemDTO read(long itemId, long userId);

    ItemDTO update(long userId, long itemId, ItemDTO itemDto);

    List<ItemDTO> getOwnersItems(long userId, int from, int size);

    @Transactional
    CommentDTO createComment(long userId, long itemId, CommentDTO commentDto);
}
