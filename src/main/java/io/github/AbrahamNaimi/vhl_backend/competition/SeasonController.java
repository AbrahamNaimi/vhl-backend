package io.github.AbrahamNaimi.vhl_backend.competition;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/seasons")
class SeasonController {

    private final SeasonService seasonService;

    SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @GetMapping
    List<SeasonResponse> list() {
        return seasonService.list();
    }

    @GetMapping("/{seasonId}")
    SeasonResponse get(@PathVariable Long seasonId) {
        return seasonService.get(seasonId);
    }

    @GetMapping("/{seasonId}/divisions")
    List<DivisionResponse> divisions(@PathVariable Long seasonId) {
        return seasonService.divisions(seasonId);
    }
}
