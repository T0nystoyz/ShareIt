package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
public interface BookingDAO extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b where b.id = ?1")
    Booking findBookingById(long bookingId);

    @Query("select b from Booking b where b.item.id = ?1")
    List<Booking> findAllByItemId(long item_id);

    @Query("select b from Booking b where b.booker.id = ?1")
    List<Booking> getAllByBookerId(long id, Pageable pageable);

    @Query("select b from Booking b where b.booker.id = ?1 and b.end < ?2")
    List<Booking> findByBookerIdAndEndBefore(long id, LocalDateTime end, Pageable pageable);

    @Query("select b from Booking b where b.booker.id = ?1 and b.start > ?2")
    List<Booking> findByBookerIdAndStartAfter(long id, LocalDateTime start, Pageable pageable);

    @Query("select b from Booking b where b.booker.id = ?1 and b.status = ?2")
    List<Booking> findByBookerIdAndStatus(long id, Status status, Pageable pageable);

    @Query("select b from Booking b where b.booker.id = ?1 and b.start < ?2 and b.end > ?2")
    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(long id, LocalDateTime current, Pageable pageable);

    @Query("select b from Booking b where b.item.owner.id = ?1")
    List<Booking> findByItemOwnerId(long id, Pageable pageable);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.end < ?2")
    List<Booking> findByItemOwnerIdAndEndBefore(long id, LocalDateTime end, Pageable pageable);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start > ?2")
    List<Booking> findByItemOwnerIdAndStartAfter(long id, LocalDateTime start, Pageable pageable);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start < ?2 and b.end > ?2")
    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfter(long item_owner_id, LocalDateTime start, Pageable pageable);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.status = ?2")
    List<Booking> findByItemOwnerIdAndStatus(long id, Status status, Pageable pageable);

    @Query("select (count(b) > 0) from Booking b where b.item.id = ?1 and b.booker.id = ?2 and b.end < ?3")
    boolean existsByItemIdAndBookerIdAndEndBefore(long itemId, long bookerId, LocalDateTime end);
}
