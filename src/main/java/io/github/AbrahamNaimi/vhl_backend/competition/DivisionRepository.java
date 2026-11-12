package io.github.AbrahamNaimi.vhl_backend.competition;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface DivisionRepository extends JpaRepository<Division, Long> {

    List<Division> findBySeasonIdOrderByLevel(Long seasonId);

    boolean existsByIdAndSeasonId(Long id, Long seasonId);
}
