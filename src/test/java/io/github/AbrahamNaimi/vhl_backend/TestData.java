package io.github.AbrahamNaimi.vhl_backend;

import org.springframework.jdbc.core.JdbcTemplate;

// The only place that inserts test data, so a new required column means changing one helper.
class TestData {

    private final JdbcTemplate jdbc;

    TestData(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    Long insertSeason(String name, String startsOn, String endsOn) {
        return jdbc.queryForObject("""
                insert into season (name, starts_on, ends_on)
                values (?, cast(? as date), cast(? as date))
                returning id
                """, Long.class, name, startsOn, endsOn);
    }

    Long insertSeason(String name) {
        return insertSeason(name, "2027-01-01", "2027-03-01");
    }

    Long insertDivision(Long seasonId, String name, int level) {
        return jdbc.queryForObject(
                "insert into division (season_id, name, level, team_count) values (?, ?, ?, 8) returning id",
                Long.class, seasonId, name, level);
    }

    Long insertTeam(String name) {
        return jdbc.queryForObject("insert into team (name) values (?) returning id", Long.class, name);
    }

    void register(Long teamId, Long seasonId, Long divisionId) {
        jdbc.update("insert into team_season (team_id, season_id, division_id) values (?, ?, ?)",
                teamId, seasonId, divisionId);
    }
}
