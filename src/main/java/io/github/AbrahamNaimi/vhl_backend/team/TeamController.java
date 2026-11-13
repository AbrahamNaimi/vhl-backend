package io.github.AbrahamNaimi.vhl_backend.team;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class TeamController {

    private final TeamService teamService;

    TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/api/seasons/{seasonId}/teams")
    List<TeamResponse> listForSeason(@PathVariable Long seasonId,
                                     @RequestParam(required = false) Long divisionId) {
        return teamService.listForSeason(seasonId, divisionId);
    }

    @GetMapping("/api/teams/{teamId}")
    TeamResponse get(@PathVariable Long teamId) {
        return teamService.get(teamId);
    }
}
