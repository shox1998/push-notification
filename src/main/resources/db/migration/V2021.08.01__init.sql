create table users
(
    id          bigint auto_increment
        primary key,
    username    varchar(255)         not null,
    password    varchar(255)         not null,
    email       varchar(255)         null,
    first_name  varchar(255)         null,
    last_name   varchar(255)         null,
    role        varchar(255)         null,
    is_locked   boolean default false not null,
    created_at  timestamp default now()    not null,
    modified_at timestamp default now() on update now()  not null,
    deleted     bit                  not null,
    deleted_at  timestamp          null,
    constraint users_email_constraint
        unique (email),
    constraint users_username_constraint
        unique (username)
);

create table pushes
(
    id          varchar(255) not null
        primary key,
    title       varchar(255) null,
    content     text         null,
    type        varchar(255) null,
    created_at  timestamp default now()     not null,
    modified_at timestamp on update now()   not null,
    deleted     boolean  default false not null,
    deleted_at  timestamp  null
);

create table notices
(
    id          varchar(255) not null
        primary key,
    title       varchar(255) null,
    content     text         null,
    type        varchar(255) null,
    from_date   timestamp         null,
    to_date     timestamp    null,
    created_at  timestamp default now()     not null,
    modified_at timestamp default now() on update now() not null,
    deleted     boolean    default false   not null,
    deleted_at  timestamp  null
);

