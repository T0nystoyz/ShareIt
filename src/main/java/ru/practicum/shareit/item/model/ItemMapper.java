package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.requests.model.ItemRequest;

@Component
public class ItemMapper {

    public static ItemDTO toDto(Item item) {
        ItemDTO itemDto = new ItemDTO();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwnerId(item.getOwner().getId());
        itemDto.setRequestId(item.getRequest() == null ? 0 : item.getRequest().getId());
        return itemDto;

    }

    public static Item toItem(ItemDTO itemDto) {
        /*Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;*/
        return Item.builder()
                .id(itemDto.getId())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }
    /*return ItemDTO.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .ownerId(item.getOwner().getId())
                    .requestId(item.getRequest().getId())
                    .build();*/
    public static Item toItem(ItemDTO itemDto, ItemRequest request) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setRequest(request);
        return item;
    }

}
