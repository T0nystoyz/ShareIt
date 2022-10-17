package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDTO userDto) {
        log.info(":::POST /users создание пользователя {}", userDto);
        return userClient.create(userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> read(@PathVariable Long userId) {
        log.info(":::GET /users чтение пользователя с id={}", userId);
        return userClient.read(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable Long userId, @RequestBody UserDTO userDto) {
        log.info(":::PATCH /users обновление пользователя с id={} следующими данными {}", userId, userDto);
        return userClient.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        log.info(":::DELETE /users удаление пользователя с id={}", userId);
        return userClient.delete(userId);
    }

    @GetMapping
    public ResponseEntity<Object> readAll() {
        log.info(":::GET /users чтение всех пользователей");
        return userClient.readAll();
    }
}
