-- USERS
create table if not exists users (
                                     id bigserial primary key,
                                     email varchar(255) not null unique,
    full_name varchar(255) not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
    );

-- API KEYS
create table if not exists api_keys (
                                        id bigserial primary key,
                                        key_hash varchar(255) not null unique,
    client_name varchar(255) not null,
    active boolean not null default true,
    user_id bigint not null references users(id),
    created_at timestamp not null default now()
    );

-- BUDGETS (soft delete + active)
create table if not exists budgets (
                                       id bigserial primary key,
                                       user_id bigint not null references users(id),
    period_type varchar(20) not null,
    year int not null,
    month int null,
    limit_amount numeric(19,2) not null,
    active boolean not null default true,
    deleted boolean not null default false,
    deleted_at timestamp null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
    );

create index if not exists ix_budgets_user_period on budgets(user_id, period_type, year, month);

-- EXPENSES
create table if not exists expenses (
                                        id bigserial primary key,
                                        user_id bigint not null references users(id),
    amount numeric(19,2) not null,
    expense_date date not null,
    title varchar(255) not null,
    description varchar(1000),
    type varchar(20) not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
    );

create index if not exists ix_expenses_user_date on expenses(user_id, expense_date);

-- ALERTS
create table if not exists alerts (
                                      id bigserial primary key,
                                      user_id bigint not null references users(id),
    budget_id bigint not null references budgets(id),
    period_type varchar(20) not null,
    year int not null,
    month int null,
    limit_amount numeric(19,2) not null,
    total_expense numeric(19,2) not null,
    type varchar(30) not null,
    message varchar(500),
    status varchar(20) not null default 'ACTIVE',
    read_at timestamp null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
    );

create index if not exists ix_alerts_user_created on alerts(user_id, created_at desc);