package org.online.queue.backend_java.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "queue", schema = "main")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "size")
    Integer size;

    @Column(name = "start_time")
    ZonedDateTime startTime;

    @Column(name = "end_time")
    ZonedDateTime endTime;

    @Column(name = "interval")
    Integer interval;

    @Column(name = "open_timestamp")
    ZonedDateTime openTimestamp;

    @Column(name = "owner_id")
    Long ownerId;

    @OneToMany(mappedBy = "queue", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Position> positions = new ArrayList<>();

    @OneToMany(mappedBy = "queue", cascade = CascadeType.ALL, orphanRemoval = true)
    List<QueueLog> queueLogs = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Queue queue = (Queue) o;
        return Objects.equals(name, queue.name) &&
                Objects.equals(description, queue.description) &&
                Objects.equals(size, queue.size) &&
                Objects.equals(startTime, queue.startTime) &&
                Objects.equals(endTime, queue.endTime) &&
                Objects.equals(interval, queue.interval) &&
                Objects.equals(openTimestamp, queue.openTimestamp) &&
                Objects.equals(ownerId, queue.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, size, startTime, endTime, interval, openTimestamp, ownerId, positions, queueLogs);
    }
}
