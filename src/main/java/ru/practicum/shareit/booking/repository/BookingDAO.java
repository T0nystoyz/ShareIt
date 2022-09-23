package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
@Transactional
public interface BookingDAO extends JpaRepository<Booking, Long> {

    Booking save(Booking booking);
    @Query("select b from Booking b where b.id = ?1")
    Booking findBookingById(long bookingId);

    @Query("select b from Booking b where b.item.id = ?1")
    List<Booking> findAllByItem_id(long item_id);

    @Query("select b from Booking b where b.booker.id = ?1")
    List<Booking> getAllByBookerId(long id);


    @Query("select b from Booking b where b.booker.id = ?1 and b.end < ?2")
    List<Booking> findByBooker_IdAndEndBefore(long id, LocalDateTime end);


    @Query("select b from Booking b where b.booker.id = ?1 and b.start > ?2")
    List<Booking> findByBooker_IdAndStartAfter(long id, LocalDateTime start);


    @Query("select b from Booking b where b.booker.id = ?1 and b.status = ?2")
    List<Booking> findByBooker_IdAndStatus(long id, Status status);

    @Query("select b from Booking b where b.booker.id = ?1 and b.start < ?2 and b.end > ?2")
    List<Booking> findByBooker_IdAndStartBeforeAndEndAfter(long id, LocalDateTime current);


    @Query("select b from Booking b where b.item.owner.id = ?1")
    List<Booking> findByItem_Owner_Id(long id);


    @Query("select b from Booking b where b.item.owner.id = ?1 and b.end < ?2")
    List<Booking> findByItem_Owner_IdAndEndBefore(long id, LocalDateTime end);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start > ?2")
    List<Booking> findByItem_Owner_IdAndStartAfter(long id, LocalDateTime start);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start < ?2 and b.end > ?2")
    List<Booking> findByItem_Owner_IdAndStartBeforeAndEndAfter(long item_owner_id, LocalDateTime start);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.status = ?2")
    List<Booking> findByItem_Owner_IdAndStatus(long id, Status status);

    @Query("select (count(b) > 0) from Booking b where b.item.id = ?1 and b.booker.id = ?2 and b.end < ?3")
    boolean existsByItem_IdAndBooker_IdAndEndBefore(long itemId, long bookerId, LocalDateTime end);
}
