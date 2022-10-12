package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentDAO;
import ru.practicum.shareit.item.repository.ItemDAO;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestDAO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDAO;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
class CommentDAOTest {
    @Autowired
    private ItemDAO itemRepository;
    @Autowired
    private UserDAO userRepository;
    @Autowired
    private ItemRequestDAO requestRepository;
    @Autowired
    private CommentDAO commentRepository;
    private final User user = new User(1, "имя", "имя@mail.ru");
    private final ItemRequest request = new ItemRequest(1, "запрос", user, LocalDateTime.of(2022, 9, 9, 12, 12, 12));
    private final Item item = new Item(1, "отвертка", "обычная", true, user, request);
    private final Comment comment = new Comment(1L, "комментарий", item, user, LocalDateTime.now());
    @BeforeEach
    void setUp() {
        userRepository.save(user);
        requestRepository.save(request);
        itemRepository.save(item);
        commentRepository.save(comment);
    }

    @Test
    void findAllByItemId() {
        List<Comment> comments = commentRepository.findAllByItemId(item.getId());

        assertThat(comments, equalTo(List.of(comment)));
    }

    @Test
    void existsCommentsByItemId() {
        assertDoesNotThrow(() -> commentRepository.existsCommentsByItemId(item.getId()));
    }
}