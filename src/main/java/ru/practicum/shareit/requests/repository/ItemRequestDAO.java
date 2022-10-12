package ru.practicum.shareit.requests.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

@Transactional
public interface ItemRequestDAO extends JpaRepository<ItemRequest, Long> {
    @Query("select i from ItemRequest i where i.requester.id = ?1 order by i.created DESC")
    List<ItemRequest> findByRequesterIdOrderByCreatedDesc(long id);
    @Query("select i from ItemRequest i where i.requester.id <> ?1 order by i.created DESC")
    List<ItemRequest> findByRequesterIdNotOrderByCreatedDesc(long id, Pageable pageable);
}
