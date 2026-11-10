create table season (
                        id                      bigint       generated always as identity primary key,
                        name                    varchar(100) not null unique,
                        starts_on               date         not null,
                        ends_on                 date         not null,
                        tokens_per_team         integer      not null default 4 check (tokens_per_team >= 0),
                        roster_min              integer      not null default 5,
                        roster_max              integer      not null default 6,
                        core_size               integer      not null default 5,
                        players_per_side        integer      not null default 4,
                        points_win_2_0          integer      not null default 3,
                        points_win_2_1          integer      not null default 2,
                        points_loss_1_2         integer      not null default 1,
                        points_loss_0_2         integer      not null default 0,
                        no_show_penalty         integer      not null default 1,
                        transfer_cooldown_hours integer      not null default 24,
                        roster_lock_at          timestamptz,
                        check (ends_on > starts_on),
                        check (roster_min <= roster_max),
                        check (core_size <= roster_max)
);

create table division (
                          id         bigint      generated always as identity primary key,
                          season_id  bigint      not null references season (id),
                          name       varchar(50) not null,
                          level      integer     not null check (level >= 1),
                          team_count integer     not null check (team_count >= 2),
                          unique (season_id, level)
);