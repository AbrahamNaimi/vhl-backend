package io.github.AbrahamNaimi.vhl_backend.team;

import java.time.Instant;

record TeamResponse(Long id, String name, Instant createdAt, Instant disbandedAt) {

    static TeamResponse from(Team team) {
        return new TeamResponse(team.getId(), team.getName(), team.getCreatedAt(), team.getDisbandedAt());
    }
}
