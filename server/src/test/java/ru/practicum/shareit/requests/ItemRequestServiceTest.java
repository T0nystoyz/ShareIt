package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.requests.model.ItemRequestDTO;
import ru.practicum.shareit.requests.model.RequestDTO;
import ru.practicum.shareit.requests.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDAO;
import ru.practicum.shareit.utils.exceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {
    private final User user = new User(1, "имя", "имя@mail.ru");
    private final User user2 = new User(2, "имя2", "имя2@mail.ru");
    private final RequestDTO requestDto = new RequestDTO(1,
            "запрос", user.getId(),
            LocalDateTime.of(2022, 9, 9, 12, 12, 12));
    private final RequestDTO requestDto2 = new RequestDTO(2,
            "запрос", user2.getId(),
            LocalDateTime.of(2022, 9, 9, 12, 12, 12));

    @Autowired
    private UserDAO userRepository;
    @Autowired
    private ItemRequestServiceImpl requestService;

    @Test
    @DirtiesContext
    void create() {
        userRepository.save(user);
        requestService.create(1, requestDto);
        ItemRequestDTO readRequest = requestService.read(1, 1);
        assertThat(readRequest.getDescription(), equalTo(requestDto.getDescription()));
    }

    @Test
    @DirtiesContext
    void createWithWrongUser() {
        assertThrows(UserNotFoundException.class, () -> requestService.create(1, requestDto));
    }

    @Test
    @DirtiesContext
    void readWithNoUser() {
        assertThrows(UserNotFoundException.class, () -> requestService.read(1, 1));
    }

    @Test
    @DirtiesContext
    void readWithNoRequest() {
        assertThrows(UserNotFoundException.class, () -> requestService.read(1, 1));
    }

    @Test
    @DirtiesContext
    void read() {
        userRepository.save(user);
        requestService.create(1, requestDto);
        ItemRequestDTO readRequest = requestService.read(1, 1);
        assertThat(readRequest.getDescription(), equalTo(requestDto.getDescription()));
    }

    @Test
    @DirtiesContext
    void getUsersRequests() {
        userRepository.save(user);
        requestService.create(1, requestDto);
        List<ItemRequestDTO> requests = requestService.getUsersRequests(1);
        requests.get(0).setCreated(LocalDateTime.now().toString());
        assertThat(requests.size(), equalTo(List.of(requestDto).size()));
        assertTrue(requests.get(0).getDescription().contains(requestDto.getDescription()));
        assertThat(requests.get(0).getId(), equalTo(requestDto.getId()));
    }

    @Test
    @DirtiesContext
    void readAll() {
        userRepository.save(user);
        userRepository.save(user2);
        requestService.create(1, requestDto);
        requestService.create(2, requestDto2);
        List<ItemRequestDTO> requests = requestService.readAll(1, 0, 10);
        requests.get(0).setCreated(LocalDateTime.now().toString());
        assertThat(requests.size(), equalTo(List.of(requestDto).size()));
        assertTrue(requests.get(0).getDescription().contains(requestDto.getDescription()));
        assertThat(requests.get(0).getId(), equalTo(requestDto2.getId()));
    }
}