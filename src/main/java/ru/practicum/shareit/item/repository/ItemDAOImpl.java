package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.IdGenerator.nextItemId;

@Repository
public class ItemDAOImpl implements ItemDAO {

    private static final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item create(Item item) {
        item.setId(nextItemId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(long itemId, Item item) {
        items.put(itemId, item);
        return item;
    }

    @Override
    public Item read(long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getOwnersItems(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findItemsByText(String text) {
        return items.values().stream()
                .filter(item -> item.getAvailable()
                        && (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());
    }
}
