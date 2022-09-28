package ru.practicum.shareit.item.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
