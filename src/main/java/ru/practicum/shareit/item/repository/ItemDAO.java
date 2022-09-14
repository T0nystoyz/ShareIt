package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDAO {
    Item create(Item item);

    Item update(long itemId, Item item);

    Item read(long itemId);

    List<Item> getOwnersItems(long userId);

    List<Item> findItemsByText(String text);
}

