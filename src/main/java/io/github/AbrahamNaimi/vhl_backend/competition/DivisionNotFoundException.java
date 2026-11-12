package io.github.AbrahamNaimi.vhl_backend.competition;

class DivisionNotFoundException extends RuntimeException {

    DivisionNotFoundException(Long seasonId, Long divisionId) {
        super("Division " + divisionId + " not found in season " + seasonId);
    }
}
