package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.repository.BookingDAO;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemDAO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDAO;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceTest {
    @MockBean
    private final UserDAO userRepository;
    @MockBean
    private final BookingDAO bookingRepository;
    @MockBean
    private final ItemDAO itemRepository;
    private final User user = new User(1, "имя", "имя@mail.ru");
    private final User user2 = new User(2, "имя", "имя@mail.ru");
    private final Item item = new Item(1, "отвертка", "обычная", true, user2, null);
    private final Booking booking = new Booking(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
            item, user, Status.WAITING);
    private final RequestBookingDTO bookingRequestDto = new RequestBookingDTO(1L, LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2), 1L, 1L, Status.WAITING);
    @Autowired
    private BookingServiceImpl service;
    private MockitoSession session;

    @BeforeEach
    void setUp() {
        session = mockitoSession().initMocks(this).startMocking();
        service = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.existsById(anyLong())).thenReturn(true);
    }

    @AfterEach
    void tearDown() {
        session.finishMocking();
    }

    @Test
    void create() {
        when(itemRepository.getReferenceById(anyLong())).thenReturn(item);
        when(bookingRepository.save(any())).thenReturn(booking);

        ResponseBookingDTO bookingResponseDto = service.create(1L, bookingRequestDto);

        assertThat(bookingResponseDto.getId(), equalTo(booking.getId()));
        assertThat(bookingResponseDto.getStart(), equalTo(booking.getStart()));
        assertThat(bookingResponseDto.getEnd(), equalTo(booking.getEnd()));
        assertThat(bookingResponseDto.getStatus(), equalTo(booking.getStatus()));
        assertThat(bookingResponseDto.getItem(), equalTo(booking.getItem()));
        assertThat(bookingResponseDto.getBooker(), equalTo(booking.getBooker()));

    }

    @Test
    void approveBooking() {
        when(bookingRepository.getReferenceById(anyLong())).thenReturn(booking);
        when(bookingRepository.save(any())).thenReturn(booking);
        when(bookingRepository.existsById(anyLong())).thenReturn(true);

        ResponseBookingDTO bookingResponseDto = service.approveBooking(2, booking.getId(), true);

        assertThat(bookingResponseDto.getId(), equalTo(booking.getId()));
        assertThat(bookingResponseDto.getStart(), equalTo(booking.getStart()));
        assertThat(bookingResponseDto.getEnd(), equalTo(booking.getEnd()));
        assertThat(bookingResponseDto.getStatus(), equalTo(booking.getStatus()));
        assertThat(bookingResponseDto.getItem(), equalTo(booking.getItem()));
        assertThat(bookingResponseDto.getBooker(), equalTo(booking.getBooker()));
    }

    @Test
    void read() {
        when(bookingRepository.getReferenceById(any())).thenReturn(booking);
        when(bookingRepository.findBookingById(anyLong())).thenReturn(booking);
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        ResponseBookingDTO bookingResponseDto = service.read(user.getId(), 1);

        assertThat(bookingResponseDto.getId(), equalTo(booking.getId()));
        assertThat(bookingResponseDto.getStart(), equalTo(booking.getStart()));
        assertThat(bookingResponseDto.getEnd(), equalTo(booking.getEnd()));
        assertThat(bookingResponseDto.getStatus(), equalTo(booking.getStatus()));
        assertThat(bookingResponseDto.getItem(), equalTo(booking.getItem()));
        assertThat(bookingResponseDto.getBooker(), equalTo(booking.getBooker()));
    }

    @Test
    void readBookingByUser() {
        when(bookingRepository.findByBookerIdAndStatus(anyLong(), any(Status.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<ResponseBookingDTO> bookings = service.readBookingByUser(1, State.WAITING, 1, 10);

        assertThat(bookings.size(), equalTo(List.of(booking).size()));
    }

    @Test
    void readBookingByOwner() {
        when(bookingRepository.findByItemOwnerIdAndStatus(anyLong(), any(Status.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<ResponseBookingDTO> bookings = service.readBookingByOwner(1, State.WAITING, 1, 10);

        assertThat(bookings.size(), equalTo(List.of(booking).size()));
    }
}