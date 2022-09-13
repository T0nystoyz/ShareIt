package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDTO;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemDAOImpl;
import ru.practicum.shareit.user.repository.UserDAO;
import ru.practicum.shareit.utils.exceptions.UserNotFoundException;
import ru.practicum.shareit.utils.exceptions.ValidationException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemDAOImpl itemRepository;
    private final UserDAO userRepository;

    @Override
    public ItemDTO create(long userId, ItemDTO itemDto) {
        checkUser(userRepository.read(userId) == null,
                String.format("пользователь с id=%d не найден.", userId),
                "Пользователь с id=%d не найден.", userId);
        validateItemDto(itemDto);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userRepository.read(userId));
        log.info("создан предмет {}.", item.getName());
        return ItemMapper.toDto(itemRepository.create(item));
    }

    private void checkUser(boolean userRepository, String userId, String format, long userId1) {
        if (userRepository) {
            log.info("Проверка существования пользователя с id={}", userId);
            throw new UserNotFoundException(String.format(format, userId1));
        }
    }

    private void validateItemDto(ItemDTO itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            log.info("попытка создать предмет без имени.");
            throw new ValidationException("предмент не может быть без имени.");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            log.info("попытка создать предмет без описания.");
            throw new ValidationException("предмет не может быть без описания.");
        }
        if (itemDto.getAvailable() == null) {
            log.info("попытка создать предмет без статуса аренды.");
            throw new ValidationException("предмет не может быть без статуса аренды.");
        }
    }

    @Override
    public ItemDTO read(long itemId) {
        log.info("чтение предмета с id={}.", itemId);
        return ItemMapper.toDto(itemRepository.read(itemId));
    }

    @Override
    public ItemDTO update(long userId, long itemId, ItemDTO itemDto) {
        Item itemForUpdate = itemRepository.read(itemId);
        log.info("попытка обновить предмет с id={}.", itemId);
        checkUser(itemForUpdate.getOwner().getId() != userId,
                "попытка обновить предмет не владельцем.",
                "Пользователь с id=%d не владелец предмета.", userId);

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
        return ItemMapper.toDto(itemRepository.update(itemId, itemForUpdate));
    }

    @Override
    public List<ItemDTO> getOwnersItems(long userId) {
        checkUser(userRepository.read(userId) == null,
                String.format("пользователь с id=%d не найден", userId),
                "Пользователь с id=%d не найден.", userId);
        log.info("предметы во владении пользователя {}.", userRepository.read(userId).getName());
        return itemRepository.getOwnersItems(userId).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> findItemsByText(long userId, String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        log.info("поиск предмета по фрагменту {}.", text);
        return itemRepository.findItemsByText(text).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
