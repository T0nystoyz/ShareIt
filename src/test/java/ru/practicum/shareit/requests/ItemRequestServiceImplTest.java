package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDTO;
import ru.practicum.shareit.item.repository.ItemDAO;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.ItemRequestDTO;
import ru.practicum.shareit.requests.model.RequestDTO;
import ru.practicum.shareit.requests.model.RequestMapper;
import ru.practicum.shareit.requests.repository.ItemRequestDAO;
import ru.practicum.shareit.requests.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDAO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {
    @MockBean
    private ItemRequestDAO requestRepository;
    @MockBean
    private UserDAO userRepository;
    @MockBean
    private ItemDAO itemRepository;
    @Autowired
    private ItemRequestServiceImpl service;
    private MockitoSession session;
    @BeforeEach
    void setUp() {
        session = mockitoSession().initMocks(this).startMocking();
        service = new ItemRequestServiceImpl(requestRepository, userRepository, itemRepository);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(requestRepository.existsById(anyLong())).thenReturn(true);
    }
    @AfterEach
    void tearDown() {
        session.finishMocking();
    }
    private final User user = new User(1, "имя", "имя@mail.ru");
    private final ItemDTO itemDto = new ItemDTO(1L, "отвертка", "обычная", true, user.getId(),
            null, null, null, 0L);
    private final Item item = new Item(1, "отвертка", "обычная", true, user, null);
    private final ItemRequest request = new ItemRequest(1, "запрос", user, LocalDateTime.of(2022, 9, 9, 12, 12, 12));
    private final RequestDTO requestDto = new RequestDTO(1,
            "запрос", user.getId(), LocalDateTime.of(2022, 9, 9, 12, 12, 12));
    @Test
    void create() {
        when(requestRepository.save(any())).thenReturn(request);

        RequestDTO itemRequestDto = service.create(user.getId(), requestDto);

        assertThat(itemRequestDto.getId(), equalTo(request.getId()));
        assertThat(itemRequestDto.getDescription(), equalTo(request.getDescription()));
        assertThat(itemRequestDto.getCreated(), equalTo(request.getCreated()));
    }

    @Test
    void read() {
        when(requestRepository.getReferenceById(anyLong())).thenReturn(RequestMapper.toItemRequest(requestDto));

        ItemRequestDTO itemRequestDto = service.read(user.getId(), request.getId());

        assertThat(itemRequestDto.getId(), equalTo(request.getId()));
        assertThat(itemRequestDto.getDescription(), equalTo(request.getDescription()));
        assertThat(itemRequestDto.getCreated(), equalTo(request.getCreated().toString()));
    }

    @Test
    void getUsersRequests() {
        when(requestRepository.findByRequesterIdOrderByCreatedDesc(anyLong())).thenReturn(List.of((request)));

        List<ItemRequestDTO> list = service.getUsersRequests(user.getId());

        assertThat(list, equalTo(List.of(RequestMapper.toItemRequestDto(request, new ArrayList<>()))));
    }

    @Test
    void readAll() {
        when(requestRepository.findByRequesterIdNotOrderByCreatedDesc(anyLong(), any(Pageable.class))).thenReturn(List.of(request));

        List<ItemRequestDTO> list = service.readAll(user.getId(), 0, 10);

        assertThat(list, equalTo(List.of(RequestMapper.toItemRequestDto(request, new ArrayList<>()))));
    }
}