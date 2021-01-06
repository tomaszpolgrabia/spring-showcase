create table users
(
    id         serial not null primary key,
    first_name text   not null,
    user_name  text   not null unique
);
