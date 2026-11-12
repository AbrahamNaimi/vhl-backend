package io.github.AbrahamNaimi.vhl_backend.competition;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;
import java.time.LocalDate;

@Entity
class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate startsOn;
    private LocalDate endsOn;
    private int tokensPerTeam;
    private int rosterMin;
    private int rosterMax;
    private int coreSize;
    private int playersPerSide;

    @Column(name = "points_win_2_0")
    private int pointsWin20;

    @Column(name = "points_win_2_1")
    private int pointsWin21;

    @Column(name = "points_loss_1_2")
    private int pointsLoss12;

    @Column(name = "points_loss_0_2")
    private int pointsLoss02;

    private int noShowPenalty;
    private int transferCooldownHours;
    private Instant rosterLockAt;

    protected Season() {
    }

    Long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    LocalDate getStartsOn() {
        return startsOn;
    }

    LocalDate getEndsOn() {
        return endsOn;
    }

    int getTokensPerTeam() {
        return tokensPerTeam;
    }

    int getRosterMin() {
        return rosterMin;
    }

    int getRosterMax() {
        return rosterMax;
    }

    int getCoreSize() {
        return coreSize;
    }

    int getPlayersPerSide() {
        return playersPerSide;
    }

    int getPointsWin20() {
        return pointsWin20;
    }

    int getPointsWin21() {
        return pointsWin21;
    }

    int getPointsLoss12() {
        return pointsLoss12;
    }

    int getPointsLoss02() {
        return pointsLoss02;
    }

    int getNoShowPenalty() {
        return noShowPenalty;
    }

    int getTransferCooldownHours() {
        return transferCooldownHours;
    }

    Instant getRosterLockAt() {
        return rosterLockAt;
    }
}
