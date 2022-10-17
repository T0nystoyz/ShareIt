package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDTO;

import java.util.List;

public interface UserService {

    User create(UserDTO userDto);

    UserDTO read(long userId);

    User update(long userId, UserDTO userDto);

    void delete(long userId);

    List<UserDTO> readAll();
}
