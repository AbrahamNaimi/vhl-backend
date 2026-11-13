package io.github.AbrahamNaimi.vhl_backend.team;

class TeamNotFoundException extends RuntimeException {

    TeamNotFoundException(Long teamId) {
        super("Team " + teamId + " not found");
    }
}
