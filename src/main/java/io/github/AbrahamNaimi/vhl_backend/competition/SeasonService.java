package io.github.AbrahamNaimi.vhl_backend.competition;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
class SeasonService {

    private final SeasonRepository seasonRepository;
    private final DivisionRepository divisionRepository;

    SeasonService(SeasonRepository seasonRepository, DivisionRepository divisionRepository) {
        this.seasonRepository = seasonRepository;
        this.divisionRepository = divisionRepository;
    }

    List<SeasonResponse> list() {
        return seasonRepository.findAllByOrderByStartsOnDesc().stream()
                .map(SeasonResponse::from)
                .toList();
    }

    SeasonResponse get(Long seasonId) {
        return seasonRepository.findById(seasonId)
                .map(SeasonResponse::from)
                .orElseThrow(() -> new SeasonNotFoundException(seasonId));
    }

    List<DivisionResponse> divisions(Long seasonId) {
        if (!seasonRepository.existsById(seasonId)) {
            throw new SeasonNotFoundException(seasonId);
        }
        return divisionRepository.findBySeasonIdOrderByLevel(seasonId).stream()
                .map(DivisionResponse::from)
                .toList();
    }
}
