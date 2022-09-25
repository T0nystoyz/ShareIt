package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.repository.BookingDAO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemDAO;
import ru.practicum.shareit.user.repository.UserDAO;
import ru.practicum.shareit.utils.exceptions.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingDAO bookingRepository;
    private final UserDAO userRepository;
    private final ItemDAO itemRepository;

    @Override
    public ResponseBookingDTO create(long userId, RequestBookingDTO requestBookingDto) {
        log.info("создание бронирования предмета для пользователя {}", userId);
        checkItemInDb(requestBookingDto.getItemId());
        Item item = itemRepository.getReferenceById(requestBookingDto.getItemId());
        isAvailable(itemRepository.getReferenceById(requestBookingDto.getItemId()));
        validateItemsOwner(userId, item);
        checkUserInDb(userId);
        validateTime(requestBookingDto);
        Booking booking = BookingMapper.toBooking(requestBookingDto);
        booking.setItem(item);
        booking.setBooker(userRepository.findById(userId));
        booking.setStatus(Status.WAITING);
        return BookingMapper.toResponseBookingDTO(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public ResponseBookingDTO approveBooking(long userId, long bookingId, boolean approved) {
        log.info("попытка одобрить аренду пользователем с id={}", userId);
        checkBookingInDb(bookingId);
        Booking booking = bookingRepository.getReferenceById(bookingId);
        validateStatus(booking);
        validateOwner(userId, bookingId);
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.toResponseBookingDTO(bookingRepository.save(booking));
    }

    @Override
    public ResponseBookingDTO read(long userId, long bookingId) {
        log.info("попытка найти бронирование с id={}", bookingId);
        checkBookingInDb(bookingId);
        Booking booking = bookingRepository.findBookingById(bookingId);
        validateOwnerOrBooker(userId, bookingId);
        return BookingMapper.toResponseBookingDTO(booking);
    }

    @Override
    public List<ResponseBookingDTO> readBookingByUser(long userId, State state) {
        checkUserInDb(userId);
        List<Booking> list;
        switch (state) {
            case CURRENT:
                list = bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now());
                break;
            case PAST:
                list = bookingRepository.findByBookerIdAndEndBefore(userId, LocalDateTime.now());
                break;
            case FUTURE:
                list = bookingRepository.findByBookerIdAndStartAfter(userId, LocalDateTime.now());
                break;
            case WAITING:
                list = bookingRepository.findByBookerIdAndStatus(userId, Status.WAITING);
                break;
            case REJECTED:
                list = bookingRepository.findByBookerIdAndStatus(userId, Status.REJECTED);
                break;
            default:
                list = bookingRepository.getAllByBookerId(userId);
        }
        return list.stream()
                .sorted(Comparator.comparing(Booking::getStart)
                        .reversed())
                .map(BookingMapper::toResponseBookingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResponseBookingDTO> readBookingByOwner(long ownerId, State state) {
        checkUserInDb(ownerId);
        List<Booking> list;
        switch (state) {
            case CURRENT:
                list = bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfter(ownerId, LocalDateTime.now());
                break;
            case PAST:
                list = bookingRepository.findByItemOwnerIdAndEndBefore(ownerId, LocalDateTime.now());
                break;
            case FUTURE:
                list = bookingRepository.findByItemOwnerIdAndStartAfter(ownerId, LocalDateTime.now());
                break;
            case WAITING:
                list = bookingRepository.findByItemOwnerIdAndStatus(ownerId, Status.WAITING);
                break;
            case REJECTED:
                list = bookingRepository.findByItemOwnerIdAndStatus(ownerId, Status.REJECTED);
                break;
            default:
                list = bookingRepository.findByItemOwnerId(ownerId);
        }
        return list.stream().sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(BookingMapper::toResponseBookingDTO).collect(Collectors.toList());
    }

    private void validateOwner(long userId, long bookingId) {
        if (bookingRepository.getReferenceById(bookingId).getItem().getOwner().getId() != userId) {
            throw new ItemNotFoundException("только владелец вещи может одобрить аренду");
        }
        log.info("авторизация собственником подтверждена: пользователь с id={} собственник предмета под id={}",
                userId, bookingRepository.getReferenceById(bookingId).getItem().getOwner().getId());
    }

    private void validateStatus(Booking booking) {
        if (!(booking.getStatus().equals(Status.WAITING))) {
            throw new ApproveNotWaitingBookingException("аренда не ожидает подтверждения");
        }
        log.info("статус WAITING подтвержден");
    }

    private void checkUserInDb(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("пользователь с id=%d не найден.", userId));
        }
        log.info("проверка существования пользователя с id={} пройдена", userId);
    }

    private void checkBookingInDb(long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new BookingNotFoundException(String.format("бронирование с id=%d не найдено.", bookingId));
        }
        log.info("проверка существования бронирования с id={} пройдена", bookingId);
    }

    private void validateTime(RequestBookingDTO requestBookingDto) {
        if (requestBookingDto.getStart().isAfter(requestBookingDto.getEnd())) {
            throw new ValidationException("начало не может быть позже окончания аренды");
        }
        log.info("валидация времени аренды прошла успешно");
    }

    private void validateOwnerOrBooker(long userId, long bookingId) {
        if (userId != bookingRepository.getReferenceById(bookingId).getItem().getOwner().getId()
                && userId != bookingRepository.getReferenceById(bookingId).getBooker().getId()) {
            throw new UserIsNotOwnerException(String.format("у пользователя c id=%d нет бронирований и предметов",
                    userId));
        }
        log.info("у пользователя с id={} есть предметы/бронирования для просмотра", userId);
    }

    private void checkItemInDb(long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ItemNotFoundException(String.format("предмет с id=%d не найден.", itemId));
        }
        log.info("проверка существования предмета с id={} пройдена", itemId);
    }

    private void validateItemsOwner(long userId, Item item) {
        if (item.getOwner().getId() == userId) {
            throw new OwnerBookingOwnItemException("владелец не может забронировать свой предмет");
        }
        log.info("пользователь не является собственником бронируемой вещи");
    }

    private void isAvailable(Item item) {
        if (item == null) {
            throw new ItemNotFoundException("такого предмета нет");
        }
        if (!(item.getAvailable())) {
            throw new ItemNotAvailableException("предмет недоступен");
        }
    }
}
