package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingDAO;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.repository.CommentDAO;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestDAO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDAO;
import ru.practicum.shareit.utils.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    private final User user = new User(1, "имя", "имя@mail.ru");
    private final ItemDTO itemDto = new ItemDTO("отвертка", "обычная", true, 1L);
    private final ItemRequest itemRequest = new ItemRequest(1, "", user,
            LocalDateTime.now());
    private final Item item = new Item(1, "", "", true, user, itemRequest);

    private final Comment comment = new Comment(1L, "комментарий", item, user,
            LocalDateTime.of(2022, 12, 12, 12, 12, 12));
    @Autowired
    private UserDAO userRepository;
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private CommentDAO commentRepository;
    @Autowired
    private ItemRequestDAO requestRepository;
    @MockBean
    private BookingDAO bookingRepository;

    @Test
    @DirtiesContext
    void create() {
        userRepository.save(user);
        ItemDTO created = itemService.create(1L, itemDto);
        ItemDTO readItem = itemService.read(1, 1);
        created.setComments(new ArrayList<>());
        assertThat(readItem, equalTo(created));
    }

    @Test
    @DirtiesContext
    void createWithComments() {
        long userid = userRepository.save(user).getId();
        itemDto.setComments(List.of(CommentMapper.toCommentDto(comment)));
        itemDto.setId(itemService.create(userid, itemDto).getId());
        itemDto.setRequestId(0L);
        commentRepository.save(comment);
        ItemDTO readItem = itemService.read(1, 1);
        assertThat(readItem, equalTo(itemDto));
    }

    @Test
    @DirtiesContext
    void createWithRequest() {
        userRepository.save(user);
        requestRepository.save(itemRequest);
        itemDto.setRequestId(1L);
        ItemDTO created = itemService.create(1L, itemDto);
        ItemDTO readItem = itemService.read(1, 1);
        created.setComments(new ArrayList<>());
        assertThat(readItem, equalTo(created));
    }

    @Test
    @DirtiesContext
    void findItemsByText() {
        userRepository.save(user);
        ItemDTO created = itemService.create(1L, itemDto);
        List<ItemDTO> items = itemService.findItemsByText(1, "обы", 1, 10);
        assertThat(items, equalTo(List.of(created)));
    }

    @Test
    @DirtiesContext
    void findItemsByEmptyText() {
        userRepository.save(user);
        itemService.create(1L, itemDto);
        List<ItemDTO> items = itemService.findItemsByText(1, "", 1, 10);
        assertThat(items, equalTo(Collections.emptyList()));
    }

    @Test
    @DirtiesContext
    void findItemsByTextWithWrongParameterFrom() {
        assertThrows(ValidationException.class, ()
                -> itemService.findItemsByText(1, "обыч", -1, 10));
    }

    @Test
    @DirtiesContext
    void createComment() {
        when(bookingRepository.existsByItemIdAndBookerIdAndEndBefore(anyLong(), anyLong(),
                any(LocalDateTime.class))).thenReturn(true);
        userRepository.save(user);
        itemDto.setComments(List.of(CommentMapper.toCommentDto(comment)));
        itemDto.setId(itemService.create(1L, itemDto).getId());
        itemDto.setRequestId(0L);
        itemService.createComment(1, 1, CommentMapper.toCommentDto(comment));
        List<CommentDTO> readComment = itemService.read(1, 1).getComments();
        assertTrue(readComment.get(0).getText().startsWith("комментарий"));
    }

    @Test
    @DirtiesContext
    void update() {
        userRepository.save(user);
        itemService.create(1L, itemDto);
        itemDto.setDescription("необычная");
        ItemDTO readUpdatedItem = itemService.update(1, 1, itemDto);
        assertThat(readUpdatedItem.getDescription(), equalTo("необычная"));
    }

    @Test
    @DirtiesContext
    void getOwnersItems() {
        userRepository.save(user);
        ItemDTO created = itemService.create(1L, itemDto);
        List<ItemDTO> ownersItems = itemService.getOwnersItems(1, 0, 10);
        assertThat(ownersItems, equalTo(List.of(created)));
    }
}