alter table users
    add column if not exists password_hash varchar(100);

create table if not exists refresh_tokens (
                                              id bigserial primary key,
                                              user_id bigint not null references users(id) on delete cascade,
    token_hash varchar(64) not null,
    expires_at timestamp not null,
    revoked_at timestamp null,
    created_at timestamp not null default now()
    );

create index if not exists idx_refresh_tokens_user_id on refresh_tokens(user_id);
create unique index if not exists uq_refresh_tokens_token_hash on refresh_tokens(token_hash);