package io.github.AbrahamNaimi.vhl_backend.team;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface TeamSeasonRepository extends JpaRepository<TeamSeason, Long> {

    List<TeamSeason> findBySeasonId(Long seasonId);

    List<TeamSeason> findBySeasonIdAndDivisionId(Long seasonId, Long divisionId);
}
