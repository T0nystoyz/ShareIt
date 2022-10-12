package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingDTOForItem;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private BookingDTOForItem lastBooking;
    private BookingDTOForItem nextBooking;
    private List<CommentDTO> comments;
    private Long requestId;

}