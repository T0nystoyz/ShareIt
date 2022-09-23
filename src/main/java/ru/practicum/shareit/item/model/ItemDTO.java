package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingDTOForItem;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private BookingDTOForItem lastBooking;
    private BookingDTOForItem nextBooking;
    private List<CommentDTO> comments = new ArrayList<>();

}