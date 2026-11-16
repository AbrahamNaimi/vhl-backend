# vhl-backend

Backend for the website of the VolleyHub League (VHL), an online league for the game
Just Volleyball: teams, rosters, schedules, results and standings.
Work in progress.

## Tech
Java 25, Spring Boot 4.1, PostgreSQL 18, Flyway, Spring Modulith,
Testcontainers, GitHub Actions. Discord login follows; the Angular frontend
lives in a separate repository, vhl-frontend.

## Run locally
Requires JDK 25 and Docker.

    docker compose up -d
    ./mvnw spring-boot:run "-Dspring-boot.run.profiles=dev"

The dev profile loads test data. The API runs on http://localhost:8080;
example requests are in http/.

## Test

    ./mvnw verify

Docker must be running; tests use a real PostgreSQL via Testcontainers.

## Structure
A modular monolith with the modules competition and team. The data model
is in docs/datamodel.md, decisions are in docs/adr.

## Glossary
| Dutch (in the league)                  | In code    |
|----------------------------------------|------------|
| seizoen                                | Season     |
| divisie                                | Division   |
| inschrijving van een team in een seizoen | TeamSeason |
| speelronde                             | Round      |
| wedstrijd                              | Match      |

## Way of working
Conventional Commits, a short-lived branch per change, squash merge after
a green build.