package io.github.AbrahamNaimi.vhl_backend.team;

import io.github.AbrahamNaimi.vhl_backend.competition.Seasons;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
class TeamService {

    private final Seasons seasons;
    private final TeamRepository teamRepository;
    private final TeamSeasonRepository teamSeasonRepository;

    TeamService(Seasons seasons, TeamRepository teamRepository, TeamSeasonRepository teamSeasonRepository) {
        this.seasons = seasons;
        this.teamRepository = teamRepository;
        this.teamSeasonRepository = teamSeasonRepository;
    }

    List<TeamResponse> listForSeason(Long seasonId, Long divisionId) {
        seasons.verifyExists(seasonId);
        if (divisionId != null) {
            seasons.verifyDivision(seasonId, divisionId);
        }

        List<TeamSeason> registrations = divisionId == null
                ? teamSeasonRepository.findBySeasonId(seasonId)
                : teamSeasonRepository.findBySeasonIdAndDivisionId(seasonId, divisionId);
        List<Long> teamIds = registrations.stream()
                .map(TeamSeason::getTeamId)
                .toList();

        // One query for all teams instead of one per team (N+1); findAllById has no fixed order.
        return teamRepository.findAllById(teamIds).stream()
                .map(TeamResponse::from)
                .sorted(Comparator.comparing(TeamResponse::name))
                .toList();
    }

    TeamResponse get(Long teamId) {
        return teamRepository.findById(teamId)
                .map(TeamResponse::from)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
    }
}
