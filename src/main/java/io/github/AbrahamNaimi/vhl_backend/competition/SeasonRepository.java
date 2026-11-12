package io.github.AbrahamNaimi.vhl_backend.competition;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface SeasonRepository extends JpaRepository<Season, Long> {

    List<Season> findAllByOrderByStartsOnDesc();
}
