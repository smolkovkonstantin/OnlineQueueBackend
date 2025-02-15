package org.online.queue.backend_java.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "position", schema = "main")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Position implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "queue_id")
    Queue queue;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

    @Column(name = "queue_number")
    Integer queueNumber;

    public Position() {}

    public Position(Integer queueNumber) {
        this.queueNumber = queueNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(queue.getId(), position.queue.getId()) &&
                Objects.equals(account.getId(), position.account.getId()) &&
                Objects.equals(queueNumber, position.queueNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queue, account, queueNumber);
    }
}
