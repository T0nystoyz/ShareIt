package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestDAO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDAO;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
class ItemRequestDAOTest {
    private final User user = new User(1, "имя", "имя@mail.ru");
    private final ItemRequest request = new ItemRequest(1, "запрос", user,
            LocalDateTime.of(2022, 9, 9, 12, 12, 12));

    @Autowired
    private ItemRequestDAO requestRepository;
    @Autowired
    private UserDAO userRepository;

    @DirtiesContext
    @Test
    void findByRequesterIdOrderByCreatedDesc() {
        userRepository.save(user);
        requestRepository.save(request);
        List<ItemRequest> requests = requestRepository.findByRequesterIdOrderByCreatedDesc(user.getId());
        assertThat(requests, equalTo(List.of(request)));
    }

    @DirtiesContext
    @Test
    void findByRequesterIdNotOrderByCreatedDesc() {
        final User user3 = new User("имя3", "имя3@mail.ru");
        final ItemRequest request3 = new ItemRequest("запрос", user3,
                LocalDateTime.of(2022, 9, 9, 12, 12, 12));
        final User user4 = new User("имя4", "имя4@mail.ru");
        final ItemRequest request4 = new ItemRequest("запрос", user4,
                LocalDateTime.of(2022, 9, 9, 12, 12, 12));
        userRepository.save(user3);
        userRepository.save(user4);
        requestRepository.save(request3);
        requestRepository.save(request4);
        List<ItemRequest> requests = requestRepository.findByRequesterIdNotOrderByCreatedDesc(user3.getId(),
                PageRequest.of(0, 10));
        assertThat(requests, equalTo(List.of(request4)));
    }
}