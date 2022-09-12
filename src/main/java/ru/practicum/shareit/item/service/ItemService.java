package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.ItemDTO;

import java.util.List;

public interface ItemService {

    List<ItemDTO> findItemsByText(long userId, String text);

    ItemDTO create(long userId, ItemDTO itemDto);

    ItemDTO read(long itemId);

    ItemDTO update(long userId, long itemId, ItemDTO itemDto);

    List<ItemDTO> getOwnersItems(long userId);
}
