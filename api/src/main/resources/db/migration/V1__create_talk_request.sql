create table public.class
(
    id       serial primary key,
    semester text not null,
    name     text not null,
    start_at date not null,
    end_at   date not null
);

create table account
(
    id             serial primary key,
    password       text not null,
    username       text not null,
    name           text not null,
    is_name_public bool      default false,
    created_at     timestamp default CURRENT_TIMESTAMP
);

create table public.talk_request
(
    id                  serial primary key,
    talk_requested_date date not null,
    talk_preferred_time time,
    talk_actually_at    timestamp,
    had_talk            bool default false,
    account_id          int  not null references account (id),
    class_id            int  not null references class (id),
    unique (account_id, class_id, talk_requested_date)
);

