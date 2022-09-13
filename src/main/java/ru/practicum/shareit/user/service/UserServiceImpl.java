package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDTO;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserDAO;
import ru.practicum.shareit.utils.exceptions.UniqueEmailException;
import ru.practicum.shareit.utils.exceptions.UserNotFoundException;
import ru.practicum.shareit.utils.exceptions.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userRepository;

    @Override
    public UserDTO create(UserDTO userDto) {
        validateUserDto(userDto);
        User user = UserMapper.toUser(userDto);
        return UserMapper.toDto(userRepository.create(user));

    }

    private void validateUserDto(UserDTO userDto) {
        if (readAll().stream().anyMatch(user -> user.getEmail().equals(userDto.getEmail()))) {
            log.info("пользователь с почтовым ящиком {} уже зарегистрирован", userDto.getEmail());
            throw new UniqueEmailException(String.format("пользователь с почтовым ящиком %s уже зарегистрирован", userDto.getEmail()));
        }
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            log.info("создание пользователя без почтового ящика");
            throw new ValidationException("нельзя создать пользователя без почтового ящика");
        }
    }

    @Override
    public UserDTO read(long userId) {
        log.info("чтение пользователя с id={}", userId);
        return UserMapper.toDto(userRepository.read(userId));
    }

    @Override
    public UserDTO update(long userId, UserDTO userDto) {
        User userForUpdate = userRepository.read(userId);
        if (userDto.getName() != null) {
            userForUpdate.setName(userDto.getName());
        }
        if (readAll().stream().anyMatch(user -> user.getEmail().equals(userDto.getEmail()))) {
            log.info("пользователь с почтовым ящиком {} уже зарегистрирован", userDto.getEmail());
            throw new UniqueEmailException(String.format("пользователь с почтовым ящиком %s уже зарегистрирован",
                    userDto.getEmail()));
        }
        if (userDto.getEmail() != null) {
            userForUpdate.setEmail(userDto.getEmail());
        }
        return UserMapper.toDto(userRepository.update(userId, userForUpdate));
    }

    @Override
    public void delete(long userId) {
        checkUserInDB(userId);
        log.info("пользователь {} удален", userRepository.read(userId).getName());
        userRepository.delete(userId);
    }

    private void checkUserInDB(long userId) {
        if (userRepository.read(userId) == null) {
            throw new UserNotFoundException(String.format("пользователя с id=%d нет в базе", userId));
        }
    }

    @Override
    public List<UserDTO> readAll() {
        return userRepository.readAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}
