alter table season rename constraint season_check to season_dates_check;
alter table season rename constraint season_check1 to season_roster_size_check;
alter table season rename constraint season_check2 to season_core_size_check;
