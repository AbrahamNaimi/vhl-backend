package io.github.AbrahamNaimi.vhl_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class SeasonApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    TestData data;

    @Test
    void returnsDivisionsSortedByLevel() throws Exception {
        Long seasonId = data.insertSeason("Test season", "2027-01-01", "2027-03-01");
        jdbc.update("insert into division (season_id, name, level, team_count) values (?, 'Division 2', 2, 10)", seasonId);
        jdbc.update("insert into division (season_id, name, level, team_count) values (?, 'Division 1', 1, 8)", seasonId);

        mockMvc.perform(get("/api/seasons/{seasonId}/divisions", seasonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Division 1"))
                .andExpect(jsonPath("$[0].seasonId").value(seasonId))
                .andExpect(jsonPath("$[0].level").value(1))
                .andExpect(jsonPath("$[0].teamCount").value(8))
                .andExpect(jsonPath("$[1].name").value("Division 2"))
                .andExpect(jsonPath("$[1].level").value(2))
                .andExpect(jsonPath("$[1].teamCount").value(10));
    }

    @Test
    void seasonWithoutDivisionsReturnsEmptyList() throws Exception {
        Long seasonId = data.insertSeason("Test season", "2027-01-01", "2027-03-01");

        mockMvc.perform(get("/api/seasons/{seasonId}/divisions", seasonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void listsNewestSeasonFirst() throws Exception {
        data.insertSeason("Older season", "2027-01-01", "2027-03-01");
        data.insertSeason("Newer season", "2027-09-01", "2027-11-01");

        mockMvc.perform(get("/api/seasons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Newer season"))
                .andExpect(jsonPath("$[1].name").value("Older season"));
    }

    @Test
    void returnsSeasonById() throws Exception {
        Long seasonId = data.insertSeason("Test season", "2027-01-01", "2027-03-01");

        mockMvc.perform(get("/api/seasons/{seasonId}", seasonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(seasonId))
                .andExpect(jsonPath("$.name").value("Test season"))
                .andExpect(jsonPath("$.startsOn").value("2027-01-01"))
                .andExpect(jsonPath("$.tokensPerTeam").value(4));
    }

    @Test
    void unknownSeasonReturnsProblemDetail() throws Exception {
        mockMvc.perform(get("/api/seasons/{seasonId}", -1))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Season -1 not found"));
    }

    @Test
    void divisionsOfUnknownSeasonReturnProblemDetail() throws Exception {
        mockMvc.perform(get("/api/seasons/{seasonId}/divisions", -1))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Season -1 not found"));
    }
}
