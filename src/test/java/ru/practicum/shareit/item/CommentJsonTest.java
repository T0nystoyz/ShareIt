package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.CommentDTO;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class CommentJsonTest {

    @Autowired
    private JacksonTester<CommentDTO> jacksonTester;

    private final CommentDTO commentDto = new CommentDTO(1L, "", "", LocalDateTime.now());

    @Test
    void CommentDtoTest() throws IOException {
        JsonContent<CommentDTO> res = jacksonTester.write(commentDto);

        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(res).extractingJsonPathStringValue("$.text").isEqualTo(commentDto.getText());
        assertThat(res).extractingJsonPathStringValue("$.authorName").isEqualTo(commentDto.getAuthorName());
    }
}