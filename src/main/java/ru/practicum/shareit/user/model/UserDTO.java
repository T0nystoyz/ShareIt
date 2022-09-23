package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserDTO {
    private long id;
    private String name;
    @Email(message = "Ошибка в написании почтового ящика")
    private String email;
}
