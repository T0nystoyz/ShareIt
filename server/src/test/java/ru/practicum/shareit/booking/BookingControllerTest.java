package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.RequestBookingDTO;
import ru.practicum.shareit.booking.model.ResponseBookingDTO;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.booking.model.Status.WAITING;

@AutoConfigureMockMvc
@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    private final ResponseBookingDTO response = new ResponseBookingDTO(5L,
            LocalDateTime.of(2022, 9, 9, 12, 12, 12),
            LocalDateTime.of(2022, 10, 9, 12, 12, 12),
            null, null, WAITING);
    private final RequestBookingDTO request = new RequestBookingDTO(5L,
            LocalDateTime.of(2022, 9, 9, 12, 0),
            LocalDateTime.of(2022, 10, 9, 12, 0),
            23L, 1L, WAITING);

    @MockBean
    BookingService service;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    @Test
    void create() throws Exception {
        when(service.create(anyLong(), any()))
                .thenReturn(response);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(response.getStatus().toString()), Status.class));
        verify(service, times(1)).create(1L, request);
    }

    @Test
    void approveBooking() throws Exception {
        when(service.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(response);

        mvc.perform(patch("/bookings/5")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.start", is(response.getStart().toString())))
                .andExpect(jsonPath("$.end", is(response.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(response.getStatus().toString())));
        verify(service, times(1)).approveBooking(1L, 5L, true);
    }

    @Test
    void getBookingById() throws Exception {
        when(service.read(anyLong(), anyLong()))
                .thenReturn(response);

        mvc.perform(get("/bookings/5")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
        verify(service, times(1)).read(1L, 5L);
    }

    @Test
    void getBookingsByUser() throws Exception {
        when(service.readBookingByUser(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(response));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(response))));
        verify(service, times(1)).readBookingByUser(1L, State.ALL, 1, 10);
    }

    @Test
    void getBookingsByOwner() throws Exception {
        when(service.readBookingByOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(response));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(response))));
    }
}
