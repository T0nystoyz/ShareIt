package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User create(@RequestBody @Valid UserDTO userDto) {
        log.info(":::POST /users создание пользователя {}", userDto);
        return userService.create(userDto);
    }

    @GetMapping("/{userId}")
    public UserDTO read(@PathVariable long userId) {
        log.info(":::GET /users чтение пользователя с id={}", userId);
        return userService.read(userId);
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable long userId,
                       @RequestBody @Valid UserDTO userDto) {
        log.info(":::PATCH /users обновление пользователя с id={} следующими данными {}", userId, userDto);
        return userService.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        log.info(":::DELETE /users удаление пользователя с id={}", userId);
        userService.delete(userId);
    }

    @GetMapping
    public List<UserDTO> readAll() {
        log.info(":::GET /users чтение всех пользователей");
        return userService.readAll();
    }
}
