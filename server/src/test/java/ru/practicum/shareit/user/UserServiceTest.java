package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDTO;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserDAO;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.utils.exceptions.UserNotFoundException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private final User user = new User(1, "имя", "имя@mail.ru");
    private final UserDTO userDto = new UserDTO(1, "имя", "имя@mail.ru");

    @Autowired
    private UserDAO userRepository;
    @Autowired
    private UserServiceImpl userService;

    @Test
    @DirtiesContext
    void create() {
        userRepository.save(user);
        User created = userService.create(userDto);
        UserDTO readUser = userService.read(1);
        assertThat(readUser, equalTo(UserMapper.toDto(created)));
    }

    @Test
    @DirtiesContext
    void read() {
        userRepository.save(user);
        User created = userService.create(userDto);
        UserDTO readUser = userService.read(1);
        assertThat(readUser, equalTo(UserMapper.toDto(created)));
    }

    @Test
    @DirtiesContext
    void readByWrongId() {
        assertThrows(UserNotFoundException.class, () -> userService.read(1));
    }

    @Test
    @DirtiesContext
    void update() {
        userRepository.save(user);
        userDto.setName("новое имя");
        userService.update(1, userDto);
        assertThat(userService.read(1).getName(), equalTo("новое имя"));
    }

    @Test
    @DirtiesContext
    void delete() {
        userRepository.save(user);
        userService.delete(1);
        assertThrows(UserNotFoundException.class, () -> userService.read(1));
    }

    @Test
    @DirtiesContext
    void readAll() {
        userRepository.save(user);
        userService.readAll();
        assertThat(userService.readAll(), equalTo(List.of(userDto)));
    }
}