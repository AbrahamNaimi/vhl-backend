package io.github.AbrahamNaimi.vhl_backend.team;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
class TeamSeason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long teamId;
    private Long seasonId;
    private Long divisionId;
    private Instant registeredAt;

    protected TeamSeason() {
    }

    Long getId() {
        return id;
    }

    Long getTeamId() {
        return teamId;
    }

    Long getSeasonId() {
        return seasonId;
    }

    Long getDivisionId() {
        return divisionId;
    }

    Instant getRegisteredAt() {
        return registeredAt;
    }
}
