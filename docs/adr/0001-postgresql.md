# ADR 001: PostgreSQL als database

Status: Geaccepteerd

Datum: 9/11/2026

## Context
De VHL-site bewaart spelers, teams, seizoenen, divisies, wedstrijden en
uitslagen, en die verwijzen naar elkaar: een uitslag hoort bij een wedstrijd,
een wedstrijd bij twee teams in een seizoen.

Veel regels van de league moeten ook gelden als twee verzoeken tegelijk
binnenkomen: een speler zit in maximaal één team, een team staat één keer in
een seizoen en een teamnaam is uniek. Een controle in Java vangt dat niet altijd
af; de database moet het weigeren.

De site gaat later naar Azure. Daar wil ik een beheerde database met
automatische backups, omdat ik de enige ontwikkelaar ben.

## Overwogen opties
- PostgreSQL
- MySQL
- MongoDB
- H2 in de tests, naast een andere database in productie

## Beslissing
Ik gebruik PostgreSQL 18: lokaal via Docker Compose, in tests via
Testcontainers en in productie als Azure Database for PostgreSQL. Het schema
beheer ik met Flyway.

Doorslaggevend zijn de partial unique indexes van PostgreSQL. Daarmee geldt
"een speler zit in maximaal één team" alleen voor rosterplekken die nog actief
zijn, terwijl oude rosterplekken als geschiedenis bewaard blijven. MySQL kent
geen partial indexes. MongoDB slaat documenten op, terwijl deze gegevens
relationeel zijn. H2 gedraagt zich anders dan PostgreSQL, waardoor een test kan
slagen die in productie faalt.

## Gevolgen
- Regels die alleen voor actieve rijen gelden, leg ik vast met een partial
  unique index (week 6–8).
- Tijden sla ik op als timestamptz; de site toont ze in de tijdzone van de
  speler.
- Tests draaien tegen dezelfde database en versie als productie.
- Lokaal draaien en testen vraagt Docker, op mijn laptop en in GitHub Actions.
- Overstappen naar een andere database wordt lastiger. Dat verwacht ik niet.
- Een beheerde database in Azure kost geld per maand; de league betaalt de
  hosting.