package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.requests.model.ItemRequestDTO;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestJsonTest {

    private final ItemRequestDTO itemRequestDto = new ItemRequestDTO(1, null, "", LocalDateTime.now().toString());
    @Autowired
    private JacksonTester<ItemRequestDTO> jacksonTester;

    @Test
    void ItemRequestJsonTest() throws IOException {
        JsonContent<ItemRequestDTO> res = jacksonTester.write(itemRequestDto);

        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(res).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestDto.getDescription());
        assertThat(res).extractingJsonPathStringValue("$.created").isEqualTo(itemRequestDto.getCreated());
        assertThat(res).extractingJsonPathArrayValue("$.items").isEqualTo(itemRequestDto.getItems());
    }
}