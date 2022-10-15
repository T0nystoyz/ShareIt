package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.BookingDTOForItem;
import ru.practicum.shareit.item.model.CommentDTO;
import ru.practicum.shareit.item.model.ItemDTO;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService service;

    @Autowired
    private MockMvc mvc;

    private final ItemDTO itemDto = new ItemDTO(23L, "отвертка", "обычная",
            true, 1L, null, null, null, 1L);
    private final ItemDTO updatedItemDto = new ItemDTO(23L, "отвертка+", "не обычная",
            true, 1L, null, null, null, 1L);

    private final CommentDTO commentDto = new CommentDTO(23L, "text", "Anton",
            LocalDateTime.of(2022, 10, 7, 22, 22, 22)
    );

    @Test
    void create() throws Exception {
        when(service.create(anyLong(), any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));
        verify(service, times(1)).create(1L, itemDto);
    }

    @Test
    void read() throws Exception {
        when(service.read(anyLong(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(get("/items/23")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));
        verify(service, times(1)).read(23L, 1L);
    }

    @Test
    void update() throws Exception {
        when(service.update(anyLong(), anyLong(), any()))
                .thenReturn(updatedItemDto);

        mvc.perform(patch("/items/23")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(updatedItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updatedItemDto.getName())))
                .andExpect(jsonPath("$.description", is(updatedItemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(updatedItemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(updatedItemDto.getRequestId()), Long.class));
    }

    @Test
    void getOwnersItems() throws Exception {
        ItemDTO itemDtoWithBooking = new ItemDTO();
        itemDtoWithBooking.setId(itemDto.getId());
        itemDtoWithBooking.setName(itemDto.getName());
        itemDtoWithBooking.setDescription(itemDto.getDescription());
        itemDtoWithBooking.setAvailable(itemDto.getAvailable());
        itemDtoWithBooking.setRequestId(itemDto.getRequestId());
        itemDtoWithBooking.setNextBooking(new BookingDTOForItem(2L, 2L,
                LocalDateTime.of(2022, 10, 7, 22, 22, 22),
                LocalDateTime.of(2022, 10, 6, 22, 22, 22)));

        when(service.getOwnersItems(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDtoWithBooking));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemDtoWithBooking))));
    }

    @Test
    void findItemsByText() throws Exception {
        when(service.findItemsByText(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "обычная"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemDto))));
    }

    @Test
    void createComment() throws Exception {
        when(service.createComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/23/comment")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.created", is(commentDto.getCreated().toString())));
    }
}
