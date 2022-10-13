package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.ItemDTO;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.repository.ItemDAO;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.ItemRequestDTO;
import ru.practicum.shareit.requests.model.RequestDTO;
import ru.practicum.shareit.requests.model.RequestMapper;
import ru.practicum.shareit.requests.repository.ItemRequestDAO;
import ru.practicum.shareit.user.repository.UserDAO;
import ru.practicum.shareit.utils.exceptions.UserNotFoundException;
import ru.practicum.shareit.utils.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestDAO requestRepository;
    private final UserDAO userRepository;
    private final ItemDAO itemRepository;

    @Override
    public RequestDTO create(long userId, RequestDTO itemRequestDto) {
        log.info("создание запроса предмета пользователем {}", userId);
        checkUserInDb(userId);
        ItemRequest itemRequest = RequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequester(userRepository.getReferenceById(userId));
        itemRequest.setCreated(LocalDateTime.now());
        return RequestMapper.toRequestDto(requestRepository.save(itemRequest));
    }

    @Override
    public ItemRequestDTO read(long userId, long requestId) {
        checkUserInDb(userId);
        checkRequestInDb(requestId);
        ItemRequestDTO itemRequestDto = RequestMapper.toItemRequestDto(requestRepository.getReferenceById(requestId));
        setItems(itemRequestDto);
        return itemRequestDto;
    }

    /*@Override
    public List<ItemRequestDTO> getUsersRequests(long userId) {
        checkUserInDb(userId);
        return requestRepository
                .findByRequesterIdOrderByCreatedDesc(userId).stream()
                .map(this::createItemRequestDto)
                .collect(Collectors.toList());
    }*/
    @Override
    public List<ItemRequestDTO> getUsersRequests(long userId) {
        checkUserInDb(userId);
        return requestRepository.findByRequesterIdOrderByCreatedDesc(userId).stream()
                .map(RequestMapper::toItemRequestDto)
                .peek(this::setItems)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDTO> readAll(long userId, int from, int size) {
        checkUserInDb(userId);
        validate(from);
        return requestRepository.findByRequesterIdNotOrderByCreatedDesc(userId,
                        PageRequest.of((from / size), size/* Sort.by(Sort.Direction.DESC, "created")*/)).stream()
                .map(RequestMapper::toItemRequestDto)
                .peek(this::setItems)
                .collect(Collectors.toList());
    }


    private void checkUserInDb(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("пользователь с id=%d не найден.", userId));
        }
        log.info("проверка существования пользователя с id={} пройдена", userId);
    }

    private void checkRequestInDb(long requestId) {
        if (!requestRepository.existsById(requestId)) {
            throw new UserNotFoundException(String.format("запрос с id=%d не найден.", requestId));
        }
        log.info("проверка существования запроса с id={} пройдена", requestId);
    }

    private void setItems(ItemRequestDTO itemRequestDto) {
        List<ItemDTO> items = itemRepository.findByRequestId(itemRequestDto.getId()).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
        itemRequestDto.setItems(items);
    }

    private void validate(int from) {
        if (from < 0) {
            throw new ValidationException("параметр from меньше 0");
        }
    }
}
