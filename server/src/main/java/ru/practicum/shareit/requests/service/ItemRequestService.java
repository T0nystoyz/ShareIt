package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.model.ItemRequestDTO;
import ru.practicum.shareit.requests.model.RequestDTO;

import java.util.List;

public interface ItemRequestService {
    RequestDTO create(long userId, RequestDTO itemRequestDto);

    ItemRequestDTO read(long userId, long requestId);

    List<ItemRequestDTO> getUsersRequests(long userId);

    List<ItemRequestDTO> readAll(long userId, int from, int size);


}