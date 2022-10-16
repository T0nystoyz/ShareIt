package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestBookingDTO {
	private Long id;
	@Future
	private LocalDateTime start;
	@Future
	private LocalDateTime end;
	private Long itemId;
	private Long booker;
	private State state;
}
