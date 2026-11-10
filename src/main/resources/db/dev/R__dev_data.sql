insert into season (name, starts_on, ends_on)
values ('Season 1', date '2027-01-11', date '2027-03-21')
    on conflict do nothing;

insert into division (season_id, name, level, team_count)
select s.id, d.name, d.level, d.team_count
from season s
         cross join (values ('Division 1', 1, 8), ('Division 2', 2, 10)) as d (name, level, team_count)
where s.name = 'Season 1'
    on conflict do nothing;

insert into team (name)
values ('Net Ninjas'), ('Block Party'), ('Spike Force'),
       ('Ace Attack'), ('Dig Deep'), ('Set Point')
    on conflict do nothing;

insert into team_season (team_id, season_id, division_id)
select t.id, s.id, d.id
from team t
         join season s on s.name = 'Season 1'
         join division d on d.season_id = s.id
    and d.level = case when t.name in ('Net Ninjas', 'Block Party', 'Spike Force') then 1 else 2 end
    on conflict do nothing;