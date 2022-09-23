package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

@Transactional
public interface UserDAO extends JpaRepository<User, Long> {
    @Query("select u from User u where u.id = ?1")
    User findById(long id);

    void deleteUserById(long id);
}
