package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Transactional
public interface ItemDAO extends JpaRepository<Item, Long> {

    @Query("select i from Item i where i.owner.id = ?1 order by i.id")
    List<Item> searchItemsByOwnerIdOrderById(long userId);

    @Query("select i from Item i where i.id = ?1")
    Item findItemById(long itemId);

    @Query("select i from Item i where upper(i.description) like upper(concat('%', ?1, '%')) and i.available = true")
    List<Item> searchItemByDescription(String description);
}

