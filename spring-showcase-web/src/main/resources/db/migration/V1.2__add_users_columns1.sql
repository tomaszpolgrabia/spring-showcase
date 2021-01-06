alter table users
    add column last_name text         not null default '',
    add column password  varchar(256) not null default '',
    add column email     text         not null default ''
