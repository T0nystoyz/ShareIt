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
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
@DataJpaTest
class ItemRequestDAOTest {
    @Autowired
    private ItemRequestDAO requestRepository;
    @Autowired
    private UserDAO userRepository;
    private final User user = new User(1, "имя", "имя@mail.ru");
    private final ItemRequest request = new ItemRequest(1, "запрос", user,
            LocalDateTime.of(2022, 9, 9, 12, 12, 12));

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
        userRepository.saveAndFlush(user);
        requestRepository.saveAndFlush(request);
        List<ItemRequest> requests = requestRepository.findByRequesterIdNotOrderByCreatedDesc(user.getId(), PageRequest.of(0, 10));
        assertThat(requests, equalTo(Collections.emptyList()));
    }
}