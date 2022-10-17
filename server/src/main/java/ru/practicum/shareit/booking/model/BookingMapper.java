package ru.practicum.shareit.booking.model;

import org.springframework.stereotype.Component;

@Component
public class BookingMapper {
    public static Booking toBooking(RequestBookingDTO requestBookingDto) {
        Booking booking = new Booking();
        booking.setId(requestBookingDto.getId());
        booking.setStart(requestBookingDto.getStart());
        booking.setEnd(requestBookingDto.getEnd());
        booking.setItem(null);
        booking.setBooker(null);
        booking.setStatus(requestBookingDto.getStatus());
        return booking;
    }

    public static ResponseBookingDTO toResponseBookingDTO(Booking booking) {
        ResponseBookingDTO dto = new ResponseBookingDTO();
        dto.setId(booking.getId());
        dto.setBooker(booking.getBooker());
        dto.setEnd(booking.getEnd());
        dto.setItem(booking.getItem());
        dto.setStart(booking.getStart());
        dto.setStatus(booking.getStatus());
        return dto;
    }

    public static BookingDTOForItem toBookingDtoForItem(Booking booking) {
        BookingDTOForItem bookingDTOForItem = new BookingDTOForItem();
        bookingDTOForItem.setId(booking.getId());
        bookingDTOForItem.setEnd(booking.getEnd());
        bookingDTOForItem.setStart(booking.getStart());
        bookingDTOForItem.setBookerId(booking.getBooker().getId());
        return bookingDTOForItem;
    }
}