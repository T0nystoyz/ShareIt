package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Transactional
public interface CommentDAO extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.item.id = ?1")
    List<Comment> findAllByItem_Id(long itemId);

    @Query("select (count(c) > 0) from Comment c where c.item.id = ?1")
    boolean existsCommentsByItem_Id(long itemId);
}
