package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDAO;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
class UserDAOTest {
    private final User user = new User(1, "имя", "имя@mail.ru");
    @Autowired
    private UserDAO userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(user);
    }

    @Test
    void findById() {
        User user1 = userRepository.findById(user.getId());
        assertThat(user1, equalTo(user));
    }

    @Test
    void deleteUserById() {
        userRepository.deleteUserById(user.getId());
        assertThat(false, equalTo(userRepository.existsById(user.getId())));
    }
}