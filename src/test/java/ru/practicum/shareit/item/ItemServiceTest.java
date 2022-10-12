package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingDAO;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.repository.CommentDAO;
import ru.practicum.shareit.item.repository.ItemDAO;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.requests.repository.ItemRequestDAO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDAO;
import ru.practicum.shareit.utils.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {
    @MockBean
    private final UserDAO userRepository;
    @MockBean
    private final BookingDAO bookingRepository;
    @MockBean
    private final ItemDAO itemRepository;
    @MockBean
    private final CommentDAO commentRepository;
    @MockBean
    private final ItemRequestDAO requestRepository;

    private final User user = new User(1, "имя", "имя@mail.ru");
    private final Item item = new Item(1, "отвертка", "обычная", true, user, null);
    private final Comment comment = new Comment(1L, "отвертка всем отверткам", item, user, LocalDateTime.now());
    private final ItemDTO itemDto = new ItemDTO(1L, "отвертка", "обычная", true, 1L,
            null, null, null, null);
    @Autowired
    private ItemServiceImpl itemService;
    private MockitoSession session;

    @BeforeEach
    void setUp() {
        session = mockitoSession().initMocks(this).startMocking();
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository,
                requestRepository);
    }

    @AfterEach
    void tearDown() {
        session.finishMocking();
    }

    @Test
    void findItemsByText() {
        when(itemRepository.searchItemByDescription(anyString(), any(PageRequest.class)))
                .thenReturn(List.of(item));
        List<ItemDTO> items = itemService.findItemsByText(user.getId(), "обы", 1, 10);

        assertThat(items, equalTo(List.of(ItemMapper.toDto(item))));
    }

    @Test
    void findItemsByTextWithWrongParameterFrom() {
        assertThrows(ValidationException.class, ()
                -> itemService.findItemsByText(1, "обыч", -1, 10));
    }

    @Test
    void create() {
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        assertThat(itemDto.getId(), equalTo(itemRepository.save(item).getId()));
        assertThat(itemDto.getName(), equalTo(itemRepository.save(item).getName()));
        assertThat(itemDto.getDescription(), equalTo(itemRepository.save(item).getDescription()));
        assertThat(itemDto.getAvailable(), equalTo(itemRepository.save(item).getAvailable()));
    }

    @Test
    void read() {
        when(userRepository.existsById(anyLong())).thenReturn(true);  //проверки на сущетсвование
        when(itemRepository.existsById(anyLong())).thenReturn(true);    //в приватных методах сервиса
        when(itemRepository.findItemById(anyLong())).thenReturn(item);
        ItemDTO itemDto = itemService.read(item.getId(), user.getId());

        assertThat(itemDto.getId(), equalTo(item.getId()));
        assertThat(itemDto.getName(), equalTo(item.getName()));
        assertThat(itemDto.getDescription(), equalTo(item.getDescription()));
        assertThat(itemDto.getAvailable(), equalTo(item.getAvailable()));
    }

    @Test
    void update() {
        when(itemRepository.save(any())).thenReturn(item);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item);
        ItemDTO itemDto = itemService.create(user.getId(), ItemMapper.toDto(item));

        assertThat(itemDto.getId(), equalTo(item.getId()));
        assertThat(itemDto.getName(), equalTo(item.getName()));
        assertThat(itemDto.getDescription(), equalTo(item.getDescription()));
        assertThat(itemDto.getAvailable(), equalTo(item.getAvailable()));
    }

    @Test
    void getOwnersItems() {
        when(itemRepository.searchItemsByOwnerIdOrderById(anyLong(), any())).thenReturn(List.of(item));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.getReferenceById(anyLong())).thenReturn(user);
        List<ItemDTO> items = itemService.getOwnersItems(user.getId(), 0, 10);

        assertThat(items, equalTo(List.of(ItemMapper.toDto(item))));
    }

    @Test
    void createComment() {
        when(bookingRepository.existsByItemIdAndBookerIdAndEndBefore(anyLong(), anyLong(), any())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(commentRepository.save(any())).thenReturn(comment);

        CommentDTO commentDto = itemService.createComment(user.getId(), item.getId(),
                CommentMapper.toCommentDto(comment));

        assertThat(commentDto.getId(), equalTo(comment.getId()));
        assertThat(commentDto.getText(), equalTo(comment.getText()));
        assertThat(commentDto.getAuthorName(), equalTo(comment.getAuthor().getName()));
        assertThat(commentDto.getCreated(), equalTo(comment.getCreated()));
    }
}