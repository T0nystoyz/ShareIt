package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
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
    private final User user = new User("имя", "имя@mail.ru");
    private final ItemRequest request = new ItemRequest("запрос", user, LocalDateTime.of(2022, 9, 9, 12, 12, 12));
    private final Item item = new Item("отвертка", "обычная", true, user, request);
    private final Comment comment = new Comment(1L, "комментарий", item, user, LocalDateTime.now());
    @Autowired
    private ItemDAO itemRepository;
    @Autowired
    private UserDAO userRepository;
    @Autowired
    private ItemRequestDAO requestRepository;
    @Autowired
    private CommentDAO commentRepository;

    @DirtiesContext
    @Test
    void findAllByItemId() {
        userRepository.save(user);
        requestRepository.save(request);
        itemRepository.save(item);
        commentRepository.save(comment);
        List<Comment> comments = commentRepository.findAllByItemId(item.getId());

        assertThat(comments, equalTo(List.of(comment)));
    }

    @DirtiesContext
    @Test
    void existsCommentsByItemId() {
        userRepository.save(user);
        requestRepository.save(request);
        itemRepository.save(item);
        commentRepository.save(comment);
        assertDoesNotThrow(() -> commentRepository.existsCommentsByItemId(item.getId()));
    }
}