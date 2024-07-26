package io.github.jonaskahn.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {
    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdAt;

    @ColumnDefault("0")
    @Column(name = "created_by")
    private Long createdBy;

    @ColumnDefault("current_timestamp()")
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant updatedAt;

    @ColumnDefault("0")
    @Column(name = "updated_by")
    private Long updatedBy;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
