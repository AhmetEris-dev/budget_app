-- Budgets constraint (sadece tablo varsa)
do $$
begin
  if exists (
    select 1
    from information_schema.tables
    where table_schema = 'public' and table_name = 'budgets'
  ) then

    if not exists (
      select 1 from pg_constraint where conname = 'ck_budgets_month_period'
    ) then
alter table budgets
    add constraint ck_budgets_month_period
        check (
            (period_type = 'MONTHLY' and month between 1 and 12)
                or
            (period_type = 'YEARLY' and month is null)
            );
end if;

end if;
end$$;

-- Alerts unique (sadece tablo varsa)
do $$
begin
  if exists (
    select 1
    from information_schema.tables
    where table_schema = 'public' and table_name = 'alerts'
  ) then

    if not exists (
      select 1 from pg_constraint where conname = 'uq_alerts_user_period_type'
    ) then
alter table alerts
    add constraint uq_alerts_user_period_type
        unique (user_id, period_type, year, month, type);
end if;

end if;
end$$;