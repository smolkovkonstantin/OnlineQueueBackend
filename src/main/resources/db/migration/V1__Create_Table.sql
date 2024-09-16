create table credentials
(
    id       bigserial primary key,
    email    varchar(255) unique not null,
    password varchar(255)        not null,
    role     varchar(255) default 'ROLE_USER'
);

create table account
(
    id             bigserial primary key,
    first_name     varchar(255) not null,
    last_name      varchar(255) not null,
    credentials_id bigint unique,
    foreign key (credentials_id) references credentials (id)
);

create table queue
(
    id             bigserial primary key,
    name           varchar(255) not null,
    size           smallint default 100,
    interval       smallint,
    start_time     timestamp with time zone,
    end_time       timestamp with time zone,
    open_timestamp timestamp with time zone,
    description    text
);

create table account_queue
(
    id         bigserial primary key,
    queue_id   bigserial,
    account_id bigserial,
    is_owner   boolean not null,
    foreign key (queue_id) references queue (id),
    foreign key (account_id) references account (id)
);

create table queue_log
(
    id          bigserial primary key,
    action      text,
    queue_id    bigint,
    action_time timestamp with time zone,
    foreign key (queue_id) references queue (id)
)