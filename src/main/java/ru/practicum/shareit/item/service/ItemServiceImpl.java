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
        if (userRepository.read(userId) == null) {
            log.info(String.format("пользователь с id=%d не найден.", userId));
            throw new UserNotFoundException(String.format("Пользователь с id=%d не найден.", userId));
        }
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
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userRepository.read(userId));
        log.info(String.format("создан предмет %s.", item.getName()));
        return ItemMapper.toDto(itemRepository.create(item));
    }

    @Override
    public ItemDTO read(long itemId) {
        log.info(String.format("чтение предмета с id=%d.", itemId));
        return ItemMapper.toDto(itemRepository.read(itemId));
    }

    @Override
    public ItemDTO update(long userId, long itemId, ItemDTO itemDto) {
        Item itemForUpdate = itemRepository.read(itemId);
        log.info(String.format("попытка обновить предмет с id=%d.", itemId));
        if (itemForUpdate.getOwner().getId() != userId) {
            log.info("попытка обновить предмет не владельцем.");
            throw new UserNotFoundException(String.format("Пользователь с id=%d не владелец предмета.", userId));
        }

        if (itemDto.getName() != null) {
            itemForUpdate.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemForUpdate.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemForUpdate.setAvailable(itemDto.getAvailable());
        }
        log.info(String.format("предмет обновлен с id=%d.", itemId));
        return ItemMapper.toDto(itemRepository.update(itemId, itemForUpdate));
    }

    @Override
    public List<ItemDTO> getOwnersItems(long userId) {
        if (userRepository.read(userId) == null) {
            log.info(String.format("пользователь с id=%d не найден", userId));
            throw new UserNotFoundException(String.format("Пользователь с id=%d не найден.", userId));
        }
        log.info(String.format("предметы во владении пользователя %s.", userRepository.read(userId).getName()));
        return itemRepository.getOwnersItems(userId).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> findItemsByText(long userId, String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        log.info(String.format("поиск предмета по фрагменту %s.", text));
        return itemRepository.findItemsByText(text).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
