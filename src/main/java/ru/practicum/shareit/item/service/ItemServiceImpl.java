package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingDAO;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.repository.CommentDAO;
import ru.practicum.shareit.item.repository.ItemDAO;
import ru.practicum.shareit.user.repository.UserDAO;
import ru.practicum.shareit.utils.exceptions.EmptyCommentException;
import ru.practicum.shareit.utils.exceptions.UserIsNotOwnerException;
import ru.practicum.shareit.utils.exceptions.UserNotFoundException;
import ru.practicum.shareit.utils.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemDAO itemRepository;
    private final UserDAO userRepository;
    private final BookingDAO bookingRepository;
    private final CommentDAO commentRepository;

    @Override
    public ItemDTO create(long userId, ItemDTO itemDto) {
        checkUserInDb(userId);
        validateItemDto(itemDto);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userRepository.getReferenceById(userId));
        log.info("создан предмет {}.", item.getName());
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDTO read(long itemId, long userId) {
        checkItemInDb(itemId);
        log.info("чтение предмета с id={} пользователем с id={}", itemId, userId);
        ItemDTO item = ItemMapper.toDto(itemRepository.findItemById(itemId));
        if (item.getOwnerId() == userId) {
            return setLastAndNextBookingDate(item);
        }
        if (commentRepository.existsCommentsByItemId(itemId)) {
            item.setComments(getComments(itemId));
        }
        return item;
    }

    @Override
    public ItemDTO update(long userId, long itemId, ItemDTO itemDto) {
        Item itemForUpdate = itemRepository.getReferenceById(itemId);
        log.info("попытка обновить предмет с id={}.", itemId);
        checkUserInDb(userId);
        checkOwnerOfItem(userId, itemId);

        if (itemDto.getName() != null) {
            itemForUpdate.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemForUpdate.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemForUpdate.setAvailable(itemDto.getAvailable());
        }
        log.info("предмет обновлен с id={}.", itemId);
        return ItemMapper.toDto(itemRepository.save(itemForUpdate));
    }

    @Override
    public List<ItemDTO> getOwnersItems(long userId) {
        checkUserInDb(userId);
        log.info("предметы во владении пользователя {}.", userRepository.getReferenceById(userId).getName());
        return itemRepository.searchItemsByOwnerIdOrderById(userId).stream()
                .map(ItemMapper::toDto)
                .map(this::setLastAndNextBookingDate)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> findItemsByText(long userId, String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        log.info("поиск предмета по фрагменту {}.", text);
        return itemRepository.searchItemByDescription(text).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO createComment(long userId, long itemId, CommentDTO commentDto) {
        Comment comment = CommentMapper.toComment(commentDto);
        checkCommentAndBooker(userId, itemId, comment);
        Item item = itemRepository.getReferenceById(itemId);
        comment.setItem(item);
        checkUserInDb(userId);
        comment.setAuthor(userRepository.findById(userId));
        log.info("комментарий создан {}.", commentDto.getText());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private void checkCommentAndBooker(long userId, long itemId, Comment comment) {
        if (comment.getText().isEmpty()) {
            throw new EmptyCommentException("нельзя создать пустой комментарий");
        }

        if (!bookingRepository.existsByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now())) {
            throw new ValidationException("только арендатор может оставлять отзывы");
        }
        log.info("проверка комментария к предмету с id={} и арендатора с id={} прошла успешно", itemId, userId);
    }

    public ItemDTO setLastAndNextBookingDate(ItemDTO item) {
        List<Booking> bookings = bookingRepository.findAllByItemId(item.getId());
        Booking last = bookings.stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .max((booking, booking1) -> booking1.getStart().compareTo(booking.getStart()))
                .orElse(null);

        Booking next = bookings.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart))
                .orElse(null);

        if (last != null) {
            item.setLastBooking(BookingMapper.toBookingDtoForItem(last));
        }
        if (next != null) {
            item.setNextBooking(BookingMapper.toBookingDtoForItem(next));
        }
        if (commentRepository.existsCommentsByItemId(item.getId())) {
            item.setComments(getComments(item.getId()));
        }
        return item;
    }

    private void checkUserInDb(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id=%d не найден.", userId));
        }
        log.info("проверка существования пользователя с id={} прошла успешно", userId);
    }

    private void validateItemDto(ItemDTO itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new ValidationException(String.format("предмент %s не может быть без имени.", itemDto.getName()));
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new ValidationException(String.format("предмет %s не может быть без описания.", itemDto.getName()));
        }
        if (itemDto.getAvailable() == null) {
            throw new ValidationException(String.format("предмет %s не может быть без статуса аренды.", itemDto.getName()));
        }
        log.info("валидация предмета {} прошла успешно", itemDto.getName());
    }

    private List<CommentDTO> getComments(long itemId) {
        return commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    private void checkItemInDb(long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new UserNotFoundException(String.format("Предмет с id=%d не найден.", itemId));
        }
        log.info("проверка существования предмета с id={} прошла успешно", itemId);
    }

    private void checkOwnerOfItem(long userId, long itemId) {
        if (itemRepository.getReferenceById(itemId).getOwner().getId() != userId) {
            throw new UserIsNotOwnerException(String.format("пользователь c id=%d не может редактировать чужой предмет", userId));
        }
    }
}
