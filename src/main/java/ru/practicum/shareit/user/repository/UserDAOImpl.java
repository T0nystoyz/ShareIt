package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.shareit.utils.IdGenerator.nextUserId;

@Repository
public class UserDAOImpl implements UserDAO {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        user.setId(nextUserId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User read(long userId) {
        return users.get(userId);
    }

    @Override
    public User update(long userId, User user) {
        users.put(userId, user);
        return user;
    }

    @Override
    public void delete(long userId) {
        users.remove(userId);
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(users.values());
    }
}
