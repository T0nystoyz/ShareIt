package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDTO;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserDAO;
import ru.practicum.shareit.utils.exceptions.UserNotFoundException;
import ru.practicum.shareit.utils.exceptions.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userRepository;

    @Override
    public User create(UserDTO userDto) {
        validateUserDto(userDto);
        User user = UserMapper.toUser(userDto);
        return userRepository.save(user);
    }


    @Override
    public UserDTO read(long userId) {
        log.info("чтение пользователя с id={}", userId);
        checkUserInDb(userId);
        return UserMapper.toDto(userRepository.findById(userId));
    }

    @Override
    public User update(long userId, UserDTO userDto) {
        UserDTO userForUpdate = UserMapper.toDto(userRepository.findById(userId));
        if (userDto.getName() != null) {
            userForUpdate.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userForUpdate.setEmail(userDto.getEmail());
        }
        return create(userForUpdate);
    }

    @Override
    public void delete(long userId) {
        checkUserInDb(userId);
        log.info("пользователь {} удален", userRepository.getReferenceById(userId).getName());
        userRepository.deleteUserById(userId);
    }

    @Override
    public List<UserDTO> readAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    private void validateUserDto(UserDTO userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new ValidationException("нельзя создать пользователя без почтового ящика");
        }
        log.info("валидация нового пользователя прошла успешно");
    }

    private void checkUserInDb(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("пользователя с id=%d нет в базе", userId));
        }
        log.info("проверка существования пользователя с id={} прошла успешно", userId);
    }
}
