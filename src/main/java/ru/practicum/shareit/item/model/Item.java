package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "items")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return getId() == item.getId() && getName().equals(item.getName()) && getDescription().equals(item.getDescription()) && getAvailable().equals(item.getAvailable()) && getOwner().equals(item.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getAvailable(), getOwner());
    }
}
