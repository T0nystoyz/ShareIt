package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.RequestBookingDTO;
import ru.practicum.shareit.booking.model.ResponseBookingDTO;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemDAO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDAO;
import ru.practicum.shareit.utils.exceptions.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    private final User user = new User(1, "имя", "имя@mail.ru");
    private final User user2 = new User(2, "имя2", "имя2@mail.ru");
    private final User user3 = new User(3, "имя3", "имя3@mail.ru");
    private final Item item = new Item(1, "", "", true, user, null);
    private final Item item2 = new Item(2, "", "", true, user, null);
    private final RequestBookingDTO bookingDto = new RequestBookingDTO(1L,
            LocalDateTime.of(2022, 12, 12, 12, 12, 12),
            LocalDateTime.of(2022, 12, 15, 12, 12, 12), 1L, 2L,
            Status.WAITING);
    private final RequestBookingDTO bookingDto2 = new RequestBookingDTO(2L,
            LocalDateTime.of(2022, 12, 12, 12, 12, 12),
            LocalDateTime.of(2022, 12, 15, 12, 12, 12), 2L, 2L,
            Status.REJECTED);
    @Autowired
    private BookingServiceImpl bookingService;
    @Autowired
    private ItemDAO itemRepository;
    @Autowired
    private UserDAO userRepository;

    @Test
    @DirtiesContext
    void create() {
        userRepository.save(user);
        userRepository.save(user2);
        itemRepository.save(item);
        ResponseBookingDTO response = bookingService.create(2, bookingDto);
        assertThat(response.getItem().getId(), equalTo(bookingDto.getItemId()));
        assertThat(response.getId(), equalTo(bookingDto.getId()));
        assertThat(response.getBooker().getId(), equalTo(bookingDto.getBooker()));
    }

    @Test
    @DirtiesContext
    void createNotAvailable() {
        userRepository.save(user2);
        item.setAvailable(false);
        itemRepository.save(item);
        assertThrows(ItemNotAvailableException.class, () -> bookingService.create(2, bookingDto));
    }

    @Test
    @DirtiesContext
    void createOnItemNotExist() {
        userRepository.save(user2);
        assertThrows(ItemNotFoundException.class, () -> bookingService.create(2, bookingDto));
    }

    @Test
    @DirtiesContext
    void createByOwnerOfItem() {
        userRepository.save(user);
        itemRepository.save(item);
        assertThrows(OwnerBookingOwnItemException.class, () -> bookingService.create(1, bookingDto));
    }

    @Test
    @DirtiesContext
    void createWithUserNotExist() {
        userRepository.save(user);
        itemRepository.save(item);
        assertThrows(UserNotFoundException.class, () -> bookingService.create(2, bookingDto));
    }

    @Test
    @DirtiesContext
    void approveBooking() {
        userRepository.save(user);
        userRepository.save(user2);
        itemRepository.save(item);
        bookingService.create(2, bookingDto);
        ResponseBookingDTO response = bookingService.approveBooking(1, 1, true);
        assertThat(response.getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    @DirtiesContext
    void rejectBooking() {
        userRepository.save(user);
        userRepository.save(user2);
        itemRepository.save(item);
        bookingService.create(2, bookingDto);
        ResponseBookingDTO response = bookingService.approveBooking(1, 1, false);
        assertThat(response.getStatus(), equalTo(Status.REJECTED));
    }

    @Test
    @DirtiesContext
    void read() {
        userRepository.save(user);
        userRepository.save(user2);
        itemRepository.save(item);
        ResponseBookingDTO response = bookingService.create(2, bookingDto);

        assertThat(response.getItem().getId(), equalTo(bookingService.read(2, 1).getId()));
        assertThat(response.getId(), equalTo(bookingService.read(2, 1).getId()));
        assertThat(response.getBooker().getId(), equalTo(bookingService.read(2, 1).getBooker().getId()));
    }

    @Test
    @DirtiesContext
    void readBookingNotExist() {
        userRepository.save(user);
        itemRepository.save(item);
        assertThrows(BookingNotFoundException.class, () -> bookingService.read(2, 1));
    }

    @Test
    @DirtiesContext
    void readBookingByUserWithNoItems() {
        userRepository.save(user);
        userRepository.save(user2);
        itemRepository.save(item);
        bookingService.create(2, bookingDto);
        assertThrows(UserIsNotOwnerException.class, () -> bookingService.read(3, 1));
    }

    @Test
    @DirtiesContext
    void readBookingByUserAll() {
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        itemRepository.save(item);
        itemRepository.save(item2);
        bookingService.create(2, bookingDto);
        bookingService.create(2, bookingDto2);
        List<ResponseBookingDTO> list = bookingService.readBookingByUser(2, State.ALL, 1, 10);
        assertThat(list.size(), equalTo(List.of(bookingDto, bookingDto2).size()));
    }

    @Test
    @DirtiesContext
    void readBookingByUserPast() {
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        itemRepository.save(item);
        itemRepository.save(item2);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingService.create(2, bookingDto);
        bookingService.create(2, bookingDto2);
        List<ResponseBookingDTO> list = bookingService.readBookingByUser(2, State.FUTURE, 1, 10);
        assertThat(list.size(), equalTo(List.of(bookingDto, bookingDto2).size()));
    }

    @Test
    @DirtiesContext
    void readBookingByUserRejected() {
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        itemRepository.save(item);
        itemRepository.save(item2);
        bookingService.create(2, bookingDto);
        bookingService.create(2, bookingDto2);
        bookingService.approveBooking(1, 2, false);
        List<ResponseBookingDTO> list = bookingService.readBookingByUser(2, State.REJECTED, 1, 10);
        assertThat(list.get(0).getStatus(), equalTo(bookingDto2.getStatus()));
    }

}