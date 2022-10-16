package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.model.UserDTO;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserJsonTest {
    @Autowired
    private JacksonTester<UserDTO> json;

    @Test
    void testUserDto() throws Exception {
        UserDTO userDto = new UserDTO(1, "имя", "имя@mail.ru");

        JsonContent<UserDTO> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("имя");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("имя@mail.ru");
    }
}