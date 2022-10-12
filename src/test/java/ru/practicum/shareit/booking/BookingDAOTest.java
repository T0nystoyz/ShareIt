package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingDAO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemDAO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDAO;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
class BookingDAOTest {
    private final User user = new User(1, "имя", "имя@mail.ru");
    private final Item item = new Item(1, "отвертка", "обычная", true, user, null);
    private final Booking booking = new Booking(1L, LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(9),
            item, user, Status.WAITING);
    @Autowired
    private ItemDAO itemRepository;
    @Autowired
    private UserDAO userRepository;
    @Autowired
    private BookingDAO bookingRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);

    }

    @Test
    void findBookingById() {
        Booking booking1 = bookingRepository.findBookingById(booking.getId());
        assertThat(booking1, equalTo(booking));
    }

    @Test
    void findAllByItemId() {
        List<Booking> bookings = bookingRepository.findAllByItemId(item.getId());
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void getAllByBookerId() {
        List<Booking> bookings = bookingRepository.getAllByBookerId(user.getId(), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByBookerIdAndEndBefore() {
        List<Booking> bookings = bookingRepository.findByBookerIdAndEndBefore(user.getId(),
                LocalDateTime.now().plusDays(10), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByBookerIdAndStartAfter() {
        List<Booking> bookings = bookingRepository.findByBookerIdAndStartAfter(user.getId(),
                LocalDateTime.now().plusDays(4), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByBookerIdAndStatus() {
        List<Booking> bookings = bookingRepository.findByBookerIdAndStatus(user.getId(), Status.WAITING, PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByBookerIdAndStartBeforeAndEndAfter() {
        List<Booking> bookings = bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(user.getId(),
                LocalDateTime.now().plusDays(6), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByItemOwnerId() {
        List<Booking> bookings = bookingRepository.findByItemOwnerId(user.getId(), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByItemOwnerIdAndEndBefore() {
        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndEndBefore(user.getId(),LocalDateTime.now().plusDays(10), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByItemOwnerIdAndStartAfter() {
        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStartAfter(user.getId(),LocalDateTime.now().plusDays(4), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByItemOwnerIdAndStartBeforeAndEndAfter() {
        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfter(user.getId(),
                LocalDateTime.now().plusDays(6), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByItemOwnerIdAndStatus() {
        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStatus(user.getId(), Status.WAITING, PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void existsByItemIdAndBookerIdAndEndBefore() {
        assertDoesNotThrow(() -> bookingRepository.existsByItemIdAndBookerIdAndEndBefore(item.getId(), user.getId(), LocalDateTime.now().plusDays(10)));
    }
}