package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDAO {
    User create(User user);

    User read(long userId);

    User update(long userId, User user);

    void delete(long userId);

    List<User> readAll();
}
