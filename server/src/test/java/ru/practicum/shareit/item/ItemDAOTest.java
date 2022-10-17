package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemDAO;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestDAO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDAO;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
class ItemDAOTest {
    private final User user = new User(1, "имя", "имя@mail.ru");
    private final ItemRequest request = new ItemRequest(1, "запрос", user, LocalDateTime.of(2022, 9, 9, 12, 12, 12));
    private final Item item = new Item(1, "отвертка", "обычная", true, user, request);
    @Autowired
    private ItemDAO itemRepository;
    @Autowired
    private UserDAO userRepository;
    @Autowired
    private ItemRequestDAO requestRepository;

    @DirtiesContext
    @Test
    void searchItemsByOwnerIdOrderById() {
        userRepository.save(user);
        requestRepository.save(request);
        itemRepository.save(item);
        List<Item> items = itemRepository.searchItemsByOwnerIdOrderById(user.getId(), PageRequest.of(0, 10));

        assertThat(items, equalTo(List.of(item)));
    }

    @DirtiesContext
    @Test
    void findItemById() {
        userRepository.save(user);
        requestRepository.save(request);
        itemRepository.save(item);
        Item item1 = itemRepository.findItemById(item.getId());

        assertThat(item1, equalTo(item));
    }

    @DirtiesContext
    @Test
    void searchItemByDescription() {
        userRepository.save(user);
        requestRepository.save(request);
        itemRepository.save(item);
        List<Item> items = itemRepository.searchItemByDescription("обычн", PageRequest.of(0, 10));

        assertThat(items, equalTo(List.of(item)));
    }

    @DirtiesContext
    @Test
    void findByRequest() {
        userRepository.save(user);
        requestRepository.save(request);
        itemRepository.save(item);
        List<Item> items = itemRepository.findByRequestId(request.getId());

        assertThat(items, equalTo(List.of(item)));
    }
}