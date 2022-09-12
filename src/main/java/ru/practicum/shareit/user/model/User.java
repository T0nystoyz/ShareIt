package ru.practicum.shareit.user.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private long id;
    private String name;
    private String email;
    private List<Item> items = new ArrayList<>();
}