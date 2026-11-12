# ADR 002: Modulaire monoliet

Status: Geaccepteerd

Datum: 10/11/2026

## Context
De VHL-site is voor één league, met 18 teams in twee divisies en daarnaast het
Open Circuit. Ik ben de enige ontwikkelaar en heb 8–10 uur per week. De site
bestaat uit duidelijke onderdelen: de competitie (seizoenen, divisies en de
stand), teams en rosters, wedstrijden en uitslagen, en accounts.

Die onderdelen hangen samen, maar moeten niet in elkaars code zitten. Als elke
klasse alles kan aanroepen, is de code na een paar maanden moeilijk te
veranderen zonder iets anders te breken.

## Overwogen opties
- Eén Spring Boot-applicatie zonder vaste indeling
- Een modulaire monoliet: één applicatie met modules waarvan de grenzen worden
  gecontroleerd
- Microservices: een aparte service per onderdeel

## Beslissing
Ik bouw één Spring Boot-applicatie met een package per module: nu competition
en team, later account en match. Elke module houdt zijn interne klassen
package-private en maakt alleen public wat andere modules nodig hebben, zoals
Seasons in competition.

Modules verwijzen naar elkaar via een id, niet via JPA-relaties. De
afhankelijkheden lopen één kant op: team gebruikt competition, match gebruikt
team en competition, en nooit andersom. Spring Modulith controleert dat in een
test, zodat een overtreding in CI faalt.

Microservices vallen af: ze kosten aparte deploys, databases en netwerkfouten,
terwijl één league die schaal niet nodig heeft. Een applicatie zonder indeling
valt af, omdat niets de grenzen tussen de onderdelen bewaakt.

## Gevolgen
- Eén build, één deploy en één database voor de backend; de Angular-frontend
  staat in een eigen repository.
- Gebruikt een module de interne code van een andere, of ontstaat er een kring
  tussen modules, dan faalt ModuleStructureTest.
- Data uit een andere module haal ik op via een public klasse en een id; dat
  kost soms een extra query.
- TeamSeason staat in team en niet in competition, omdat anders de twee
  modules elkaar zouden gebruiken.
- Wil een module later een eigen service worden, dan liggen de grenzen al
  vast. Dat is geen doel.