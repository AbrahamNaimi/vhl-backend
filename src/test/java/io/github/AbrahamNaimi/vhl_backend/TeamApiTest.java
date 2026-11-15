package io.github.AbrahamNaimi.vhl_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class TeamApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestData data;

    @Test
    void listsAllTeamsOfSeasonSortedByName() throws Exception {
        Long seasonId = data.insertSeason("Test season", "2027-01-01", "2027-03-01");
        Long division1 = data.insertDivision(seasonId, "Division 1", 1);
        Long division2 = data.insertDivision(seasonId, "Division 2", 2);
        data.register(data.insertTeam("Spike Force"), seasonId, division1);
        data.register(data.insertTeam("Ace Attack"), seasonId, division2);
        data.register(data.insertTeam("Open Team"), seasonId, null);

        mockMvc.perform(get("/api/seasons/{seasonId}/teams", seasonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").value(contains("Ace Attack", "Open Team", "Spike Force")));
    }

    @Test
    void listsAllFieldsOfATeam() throws Exception {
        Long seasonId = data.insertSeason("Test season", "2027-01-01", "2027-03-01");
        Long teamId = data.insertTeam("Open Team");
        data.register(teamId, seasonId, null);

        mockMvc.perform(get("/api/seasons/{seasonId}/teams", seasonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(teamId))
                .andExpect(jsonPath("$[0].name").value("Open Team"))
                .andExpect(jsonPath("$[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$[0].disbandedAt").isEmpty());
    }

    @Test
    void seasonWithoutTeamsReturnsEmptyList() throws Exception {
        Long seasonId = data.insertSeason("Test season", "2027-01-01", "2027-03-01");

        mockMvc.perform(get("/api/seasons/{seasonId}/teams", seasonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void listsOnlyTeamsOfRequestedDivision() throws Exception {
        Long seasonId = data.insertSeason("Test season", "2027-01-01", "2027-03-01");
        Long division1 = data.insertDivision(seasonId, "Division 1", 1);
        Long division2 = data.insertDivision(seasonId, "Division 2", 2);
        data.register(data.insertTeam("Spike Force"), seasonId, division1);
        data.register(data.insertTeam("Net Ninjas"), seasonId, division1);
        data.register(data.insertTeam("Ace Attack"), seasonId, division2);
        data.register(data.insertTeam("Open Team"), seasonId, null);

        mockMvc.perform(get("/api/seasons/{seasonId}/teams", seasonId).param("divisionId", division1.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").value(contains("Net Ninjas", "Spike Force")));
    }

    @Test
    void divisionOfOtherSeasonReturnsProblemDetail() throws Exception {
        Long seasonId = data.insertSeason("Test season", "2027-01-01", "2027-03-01");
        Long otherSeasonId = data.insertSeason("Other season", "2027-09-01", "2027-11-01");
        Long otherDivision = data.insertDivision(otherSeasonId, "Division 1", 1);

        mockMvc.perform(get("/api/seasons/{seasonId}/teams", seasonId).param("divisionId", otherDivision.toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Division " + otherDivision + " not found in season " + seasonId));
    }

    @Test
    void teamOfOtherSeasonIsNotListed() throws Exception {
        Long seasonId = data.insertSeason("Test season", "2027-01-01", "2027-03-01");
        Long otherSeasonId = data.insertSeason("Other season", "2027-09-01", "2027-11-01");
        data.register(data.insertTeam("Net Ninjas"), seasonId, null);
        data.register(data.insertTeam("Elsewhere"), otherSeasonId, null);

        mockMvc.perform(get("/api/seasons/{seasonId}/teams", seasonId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").value(contains("Net Ninjas")));
    }

    @Test
    void teamsOfUnknownSeasonReturnProblemDetail() throws Exception {
        mockMvc.perform(get("/api/seasons/{seasonId}/teams", -1))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Season -1 not found"));
    }

    @Test
    void returnsTeamById() throws Exception {
        Long teamId = data.insertTeam("Net Ninjas");

        mockMvc.perform(get("/api/teams/{teamId}", teamId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(teamId))
                .andExpect(jsonPath("$.name").value("Net Ninjas"))
                .andExpect(jsonPath("$.disbandedAt").isEmpty());
    }

    @Test
    void unknownTeamReturnsProblemDetail() throws Exception {
        mockMvc.perform(get("/api/teams/{teamId}", -1))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Team -1 not found"));
    }
}
