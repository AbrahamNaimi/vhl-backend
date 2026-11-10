create table team (
                      id           bigint      generated always as identity primary key,
                      name         varchar(50) not null,
                      created_at   timestamptz not null default now(),
                      disbanded_at timestamptz
);

create unique index team_name_unique_idx on team (lower(name));

create table team_season (
                             id            bigint      generated always as identity primary key,
                             team_id       bigint      not null references team (id),
                             season_id     bigint      not null references season (id),
                             division_id   bigint      references division (id),
                             registered_at timestamptz not null default now(),
                             unique (team_id, season_id)
);

create index team_season_season_idx on team_season (season_id);
create index team_season_division_idx on team_season (division_id);