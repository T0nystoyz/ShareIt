package ru.practicum.shareit.requests.model;

import ru.practicum.shareit.item.model.ItemDTO;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class RequestMapper {
    public static RequestDTO toRequestDto(ItemRequest itemRequest) {
        return RequestDTO.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requesterId(itemRequest.getRequester().getId())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequest toItemRequest(RequestDTO itemRequestDto) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated())
                .build();
    }

    public static ItemRequestDTO toItemRequestDto(ItemRequest itemRequest, List<ItemDTO> items) {
        return ItemRequestDTO.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
                .items(items)
                .build();
    }
}
