package io.github.AbrahamNaimi.vhl_backend.competition;

class SeasonNotFoundException extends RuntimeException {

    SeasonNotFoundException(Long seasonId) {
        super("Season " + seasonId + " not found");
    }
}
