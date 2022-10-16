package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.RequestBookingDTO;
import ru.practicum.shareit.booking.model.ResponseBookingDTO;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingJsonTest {

    @Autowired
    private JacksonTester<RequestBookingDTO> jacksonTesterRequest;

    @Autowired
    private JacksonTester<ResponseBookingDTO> jacksonTesterResponse;
    private final User user = new User(1, "имя", "имя@mail.ru");
    private final Item item = new Item(1, "отвертка", "обычная", true, null,
            null);
    private final RequestBookingDTO bookingRequestDto = new RequestBookingDTO(1L,
            LocalDateTime.of(2022, 9, 9, 12, 0, 0),
            LocalDateTime.of(2022, 10, 9, 12, 0, 0),
            1L, 1L, Status.WAITING);

    private final ResponseBookingDTO bookingResponseDto = new ResponseBookingDTO(1L,
            LocalDateTime.of(2022, 9, 9, 12, 0, 0),
            LocalDateTime.of(2022, 10, 9, 12, 0, 0),
            item, user, Status.WAITING);

    @Test
    void bookingRequestDtoTest() throws IOException {
        JsonContent<RequestBookingDTO> result = jacksonTesterRequest.write(bookingRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .contains(bookingRequestDto.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .contains(bookingRequestDto.getEnd().toString());
    }

    @Test
    void bookingResponseDtoTest() throws IOException {
        JsonContent<ResponseBookingDTO> result = jacksonTesterResponse.write(bookingResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .contains(bookingResponseDto.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .contains(bookingResponseDto.getEnd().toString());
    }

}