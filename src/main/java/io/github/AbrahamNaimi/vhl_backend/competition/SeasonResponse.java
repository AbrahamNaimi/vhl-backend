package io.github.AbrahamNaimi.vhl_backend.competition;

import java.time.Instant;
import java.time.LocalDate;

record SeasonResponse(
        Long id,
        String name,
        LocalDate startsOn,
        LocalDate endsOn,
        int tokensPerTeam,
        int rosterMin,
        int rosterMax,
        int coreSize,
        int playersPerSide,
        int pointsWin20,
        int pointsWin21,
        int pointsLoss12,
        int pointsLoss02,
        int noShowPenalty,
        int transferCooldownHours,
        Instant rosterLockAt
) {

    static SeasonResponse from(Season season) {
        return new SeasonResponse(
                season.getId(),
                season.getName(),
                season.getStartsOn(),
                season.getEndsOn(),
                season.getTokensPerTeam(),
                season.getRosterMin(),
                season.getRosterMax(),
                season.getCoreSize(),
                season.getPlayersPerSide(),
                season.getPointsWin20(),
                season.getPointsWin21(),
                season.getPointsLoss12(),
                season.getPointsLoss02(),
                season.getNoShowPenalty(),
                season.getTransferCooldownHours(),
                season.getRosterLockAt()
        );
    }
}
