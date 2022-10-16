package ru.practicum.shareit.requests.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false)
    private long id;
    @NotNull
    private String description;

    @ManyToOne
    private User requester;

    private LocalDateTime created;

    public ItemRequest(String description, User requester, LocalDateTime created) {
        this.description = description;
        this.requester = requester;
        this.created = created;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemRequest request = (ItemRequest) o;

        if (getId() != request.getId()) return false;
        if (getDescription() != null ? !getDescription().equals(request.getDescription()) :
                request.getDescription() != null)
            return false;
        if (getRequester() != null ? !getRequester().equals(request.getRequester()) : request.getRequester() != null)
            return false;
        return getCreated() != null ? getCreated().equals(request.getCreated()) : request.getCreated() == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, requester, created);
    }
}