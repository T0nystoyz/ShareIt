package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
public class UserDTO {
    private long id;
    private String name;
    @Email(message = "Ошибка в напсиании почтового ящика")
    private String email;
}
