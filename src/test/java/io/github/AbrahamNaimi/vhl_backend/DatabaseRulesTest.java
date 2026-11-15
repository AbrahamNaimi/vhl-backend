package io.github.AbrahamNaimi.vhl_backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

// One rule per test: after an error PostgreSQL rejects the rest of the transaction.
@IntegrationTest
class DatabaseRulesTest {

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    TestData data;

    // Team

    @Test
    void teamNamesAreUniqueIgnoringCase() {
        jdbc.update("insert into team (name) values ('Net Ninjas')");

        assertRejectedBy("team_name_unique_idx",
                () -> jdbc.update("insert into team (name) values ('net ninjas')"));
    }

    // TeamSeason

    @Test
    void teamPlaysAtMostOnceInASeason() {
        Long seasonId = data.insertSeason("Test season");
        Long teamId = data.insertTeam("Net Ninjas");
        jdbc.update("insert into team_season (team_id, season_id) values (?, ?)", teamId, seasonId);

        assertRejectedBy("team_season_team_id_season_id_key",
                () -> jdbc.update("insert into team_season (team_id, season_id) values (?, ?)", teamId, seasonId));
    }

    @Test
    void teamCanPlayInSeveralSeasons() {
        Long teamId = data.insertTeam("Net Ninjas");
        jdbc.update("insert into team_season (team_id, season_id) values (?, ?)", teamId, data.insertSeason("Season 1"));

        assertDoesNotThrow(() -> jdbc.update(
                "insert into team_season (team_id, season_id) values (?, ?)", teamId, data.insertSeason("Season 2")));
    }

    @Test
    void teamSeasonNeedsAnExistingTeam() {
        Long seasonId = data.insertSeason("Test season");

        assertRejectedBy("team_season_team_id_fkey",
                () -> jdbc.update("insert into team_season (team_id, season_id) values (-1, ?)", seasonId));
    }

    @Test
    void teamSeasonNeedsAnExistingSeason() {
        Long teamId = data.insertTeam("Net Ninjas");

        assertRejectedBy("team_season_season_id_fkey",
                () -> jdbc.update("insert into team_season (team_id, season_id) values (?, -1)", teamId));
    }

    @Test
    void teamSeasonNeedsAnExistingDivision() {
        Long seasonId = data.insertSeason("Test season");
        Long teamId = data.insertTeam("Net Ninjas");

        assertRejectedBy("team_season_division_id_fkey", () -> jdbc.update(
                "insert into team_season (team_id, season_id, division_id) values (?, ?, -1)", teamId, seasonId));
    }

    // Season

    @Test
    void seasonCannotEndBeforeItStarts() {
        assertRejectedBy("season_dates_check", () -> jdbc.update("""
                insert into season (name, starts_on, ends_on)
                values ('Backwards season', date '2027-03-01', date '2027-01-01')
                """));
    }

    @Test
    void seasonNamesAreUnique() {
        data.insertSeason("Test season");

        assertRejectedBy("season_name_key", () -> data.insertSeason("Test season"));
    }

    @Test
    void tokensPerTeamCannotBeNegative() {
        assertRejectedBy("season_tokens_per_team_check", () -> jdbc.update("""
                insert into season (name, starts_on, ends_on, tokens_per_team)
                values ('Test season', date '2027-01-01', date '2027-03-01', -1)
                """));
    }

    @Test
    void rosterMinCannotExceedRosterMax() {
        assertRejectedBy("season_roster_size_check", () -> jdbc.update("""
                insert into season (name, starts_on, ends_on, roster_min, roster_max)
                values ('Test season', date '2027-01-01', date '2027-03-01', 7, 6)
                """));
    }

    @Test
    void coreSizeCannotExceedRosterMax() {
        assertRejectedBy("season_core_size_check", () -> jdbc.update("""
                insert into season (name, starts_on, ends_on, core_size, roster_max)
                values ('Test season', date '2027-01-01', date '2027-03-01', 7, 6)
                """));
    }

    // Division

    @Test
    void divisionLevelStartsAtOne() {
        Long seasonId = data.insertSeason("Test season");

        assertRejectedBy("division_level_check", () -> jdbc.update(
                "insert into division (season_id, name, level, team_count) values (?, 'Division 0', 0, 8)", seasonId));
    }

    @Test
    void divisionNeedsAtLeastTwoTeams() {
        Long seasonId = data.insertSeason("Test season");

        assertRejectedBy("division_team_count_check", () -> jdbc.update(
                "insert into division (season_id, name, level, team_count) values (?, 'Division 1', 1, 1)", seasonId));
    }

    @Test
    void divisionLevelIsUniqueWithinASeason() {
        Long seasonId = data.insertSeason("Test season");
        jdbc.update("insert into division (season_id, name, level, team_count) values (?, 'Division 1', 1, 8)", seasonId);

        assertRejectedBy("division_season_id_level_key", () -> jdbc.update(
                "insert into division (season_id, name, level, team_count) values (?, 'Other division', 1, 8)", seasonId));
    }

    @Test
    void sameLevelInDifferentSeasonsIsAllowed() {
        jdbc.update("insert into division (season_id, name, level, team_count) values (?, 'Division 1', 1, 8)",
                data.insertSeason("Season 1"));

        assertDoesNotThrow(() -> jdbc.update(
                "insert into division (season_id, name, level, team_count) values (?, 'Division 1', 1, 8)",
                data.insertSeason("Season 2")));
    }

    @Test
    void divisionBelongsToAnExistingSeason() {
        assertRejectedBy("division_season_id_fkey", () -> jdbc.update(
                "insert into division (season_id, name, level, team_count) values (-1, 'Division 1', 1, 8)"));
    }

    // The quotes make sure a name does not also match a longer name that starts the same.
    private static void assertRejectedBy(String constraint, Executable insert) {
        var exception = assertThrows(DataIntegrityViolationException.class, insert);
        assertThat(exception.getMessage()).contains("\"" + constraint + "\"");
    }
}
