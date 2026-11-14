package io.github.AbrahamNaimi.vhl_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Transactional
class SeasonApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JdbcTemplate jdbc;

    @Test
    void returnsDivisionsSortedByLevel() throws Exception {
        Long seasonId = insertSeason("Test season", "2027-01-01", "2027-03-01");
        jdbc.update("insert into division (season_id, name, level, team_count) values (?, 'Division 2', 2, 10)", seasonId);
        jdbc.update("insert into division (season_id, name, level, team_count) values (?, 'Division 1', 1, 8)", seasonId);

        mockMvc.perform(get("/api/seasons/{seasonId}/divisions", seasonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Division 1"))
                .andExpect(jsonPath("$[1].name").value("Division 2"));
    }

    @Test
    void listsNewestSeasonFirst() throws Exception {
        insertSeason("Older season", "2027-01-01", "2027-03-01");
        insertSeason("Newer season", "2027-09-01", "2027-11-01");

        mockMvc.perform(get("/api/seasons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Newer season"))
                .andExpect(jsonPath("$[1].name").value("Older season"));
    }

    @Test
    void returnsSeasonById() throws Exception {
        Long seasonId = insertSeason("Test season", "2027-01-01", "2027-03-01");

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

    private Long insertSeason(String name, String startsOn, String endsOn) {
        return jdbc.queryForObject("""
                insert into season (name, starts_on, ends_on)
                values (?, cast(? as date), cast(? as date))
                returning id
                """, Long.class, name, startsOn, endsOn);
    }
}
