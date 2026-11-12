package io.github.AbrahamNaimi.vhl_backend.competition;

record DivisionResponse(Long id, Long seasonId, String name, int level, int teamCount) {

    static DivisionResponse from(Division division) {
        return new DivisionResponse(
                division.getId(),
                division.getSeasonId(),
                division.getName(),
                division.getLevel(),
                division.getTeamCount()
        );
    }
}
