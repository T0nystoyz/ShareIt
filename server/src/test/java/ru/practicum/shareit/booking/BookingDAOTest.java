package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
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
    private final User user = new User("имя", "имя@mail.ru");
    private final Item item = new Item("отвертка", "обычная", true, user, null);
    private final Booking booking = new Booking(1L, LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(9),
            item, user, Status.WAITING);
    @Autowired
    private ItemDAO itemRepository;
    @Autowired
    private UserDAO userRepository;
    @Autowired
    private BookingDAO bookingRepository;

    @DirtiesContext
    @Test
    void findBookingById() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);
        Booking booking1 = bookingRepository.findBookingById(booking.getId());
        assertThat(booking1, equalTo(booking));
    }

    @DirtiesContext
    @Test
    void findAllByItemId() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);
        List<Booking> bookings = bookingRepository.findAllByItemId(item.getId());
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @DirtiesContext
    @Test
    void getAllByBookerId() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);
        List<Booking> bookings = bookingRepository.getAllByBookerId(user.getId(), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @DirtiesContext
    @Test
    void findByBookerIdAndEndBefore() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);
        List<Booking> bookings = bookingRepository.findByBookerIdAndEndBefore(user.getId(),
                LocalDateTime.now().plusDays(10), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @DirtiesContext
    @Test
    void findByBookerIdAndStartAfter() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);
        List<Booking> bookings = bookingRepository.findByBookerIdAndStartAfter(user.getId(),
                LocalDateTime.now().plusDays(4), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @DirtiesContext
    @Test
    void findByBookerIdAndStatus() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);
        List<Booking> bookings = bookingRepository.findByBookerIdAndStatus(user.getId(), Status.WAITING, PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @DirtiesContext
    @Test
    void findByBookerIdAndStartBeforeAndEndAfter() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);
        List<Booking> bookings = bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(user.getId(),
                LocalDateTime.now().plusDays(6), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @DirtiesContext
    @Test
    void findByItemOwnerId() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);
        List<Booking> bookings = bookingRepository.findByItemOwnerId(user.getId(), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @DirtiesContext
    @Test
    void findByItemOwnerIdAndEndBefore() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);
        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndEndBefore(user.getId(), LocalDateTime.now().plusDays(10), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @DirtiesContext
    @Test
    void findByItemOwnerIdAndStartAfter() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);
        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStartAfter(user.getId(), LocalDateTime.now().plusDays(4), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @DirtiesContext
    @Test
    void findByItemOwnerIdAndStartBeforeAndEndAfter() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);
        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfter(user.getId(),
                LocalDateTime.now().plusDays(6), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @DirtiesContext
    @Test
    void findByItemOwnerIdAndStatus() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);
        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStatus(user.getId(), Status.WAITING, PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @DirtiesContext
    @Test
    void existsByItemIdAndBookerIdAndEndBefore() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);
        assertDoesNotThrow(() -> bookingRepository.existsByItemIdAndBookerIdAndEndBefore(item.getId(), user.getId(), LocalDateTime.now().plusDays(10)));
    }
}