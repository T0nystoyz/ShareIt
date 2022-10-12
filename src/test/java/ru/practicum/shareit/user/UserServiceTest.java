package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDTO;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserDAO;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {
    @MockBean
    private final UserDAO userRepository;
    private final User user = new User(1, "имя", "имя@mail.ru");
    private MockitoSession session;
    @Autowired
    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        session = mockitoSession().initMocks(this).startMocking();
        service = new UserServiceImpl(userRepository);
        when(userRepository.existsById(anyLong())).thenReturn(true);
    }

    @AfterEach
    void tearDown() {
        session.finishMocking();
    }

    @Test
    void create() {
        when(userRepository.save(any())).thenReturn(user);

        User userResponse = service.create(UserMapper.toDto(user));

        assertThat(userResponse.getId(), equalTo(user.getId()));
        assertThat(userResponse.getName(), equalTo(user.getName()));
        assertThat(userResponse.getEmail(), equalTo(user.getEmail()));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void read() {
        when(userRepository.findById(anyLong())).thenReturn(user);

        UserDTO userDto = UserMapper.toDto(userRepository.findById(user.getId()));

        assertThat(userDto.getId(), equalTo(user.getId()));
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void update() {
        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(user);

        User userResponse = service.create(UserMapper.toDto(user));

        assertThat(userResponse.getId(), equalTo(user.getId()));
        assertThat(userResponse.getName(), equalTo(user.getName()));
        assertThat(userResponse.getEmail(), equalTo(user.getEmail()));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void delete() {
        service.delete(user.getId());

        verify(userRepository, times(1)).deleteUserById(anyLong());
    }

    @Test
    void readAll() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> users = service.readAll();

        assertThat(users, equalTo(List.of(UserMapper.toDto(user))));
        verify(userRepository, times(1)).findAll();
    }
}