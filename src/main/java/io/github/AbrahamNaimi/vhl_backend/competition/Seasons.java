package io.github.AbrahamNaimi.vhl_backend.competition;

import org.springframework.stereotype.Service;

@Service
public class Seasons {

    private final SeasonRepository seasonRepository;
    private final DivisionRepository divisionRepository;

    Seasons(SeasonRepository seasonRepository, DivisionRepository divisionRepository) {
        this.seasonRepository = seasonRepository;
        this.divisionRepository = divisionRepository;
    }

    public void verifyExists(Long seasonId) {
        if (!seasonRepository.existsById(seasonId)) {
            throw new SeasonNotFoundException(seasonId);
        }
    }

    public void verifyDivision(Long seasonId, Long divisionId) {
        if (!divisionRepository.existsByIdAndSeasonId(divisionId, seasonId)) {
            throw new DivisionNotFoundException(seasonId, divisionId);
        }
    }
}
