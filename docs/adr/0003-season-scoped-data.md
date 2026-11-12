# ADR 003: Alle competitiedata hoort bij een seizoen

Status: Geaccepteerd

Datum: 10/11/2026

## Context
De league speelt in seizoenen. Een team speelt meestal meerdere seizoenen,
maar veel gegevens gelden voor één seizoen: de divisie waarin het team speelt,
de 5 core-spelers, de 4 roster tokens, strafpunten en de wedstrijden. Na elk
seizoen kan een team promoveren of degraderen, en tokens en core-spelers
beginnen opnieuw.

Sla ik de divisie of de tokens direct op het team op, dan overschrijft het
volgende seizoen die van het vorige. Dan is niet meer na te gaan in welke
divisie een team vorig seizoen speelde, en kloppen oude standen niet meer.

## Overwogen opties
- Seizoensgegevens als kolommen op team, die ik bij elk nieuw seizoen
  overschrijf
- Een kopie van het team per seizoen
- Een aparte tabel team_season, met één rij per team per seizoen

## Beslissing
Een team bewaart alleen wat altijd geldt, zoals de naam. Alles wat voor één
seizoen geldt, hangt aan team_season: de divisie, de core-spelers, de tokens,
de strafpunten en later de wedstrijden. Een team staat hoogstens één keer in
een seizoen; de database dwingt dat af met een unique op (team_id, season_id).
Speelt een team in het Open Circuit, dan is division_id leeg.

Paden in de API beginnen met /api/seasons/{seasonId} voor alles wat bij een
seizoen hoort, zoals /api/seasons/{seasonId}/teams.

Kolommen op team vallen af, omdat ze de geschiedenis overschrijven. Een kopie
van het team per seizoen valt af, omdat de naam en de geschiedenis van één
team dan over meerdere rijen verspreid raken.

## Gevolgen
- Oude seizoenen blijven compleet bewaard, zodat een seizoensarchief later
  weinig werk is.
- Promotie en degradatie zijn een nieuwe rij in team_season voor het volgende
  seizoen; er wordt niets overschreven.
- Een nieuwe teamnaam of captain verandert alleen team, en zet tokens, stand en
  strafpunten niet terug (US-5.10).
- Veel queries hebben een extra join met team_season nodig.
- De site moet bepalen welk seizoen het huidige is, bijvoorbeeld het seizoen
  waarvan de datums vandaag bevatten. Dat regel ik zodra de eerste schermen het
  nodig hebben.