package io.github.AbrahamNaimi.vhl_backend.team;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Instant createdAt;
    private Instant disbandedAt;

    protected Team() {
    }

    Long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    Instant getCreatedAt() {
        return createdAt;
    }

    Instant getDisbandedAt() {
        return disbandedAt;
    }
}
