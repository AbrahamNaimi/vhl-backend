package io.github.AbrahamNaimi.vhl_backend.competition;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
class Division {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long seasonId;
    private String name;
    private int level;
    private int teamCount;

    protected Division() {
    }

    Long getId() {
        return id;
    }

    Long getSeasonId() {
        return seasonId;
    }

    String getName() {
        return name;
    }

    int getLevel() {
        return level;
    }

    int getTeamCount() {
        return teamCount;
    }
}
