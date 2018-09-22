create table task (
    id serial primary key,
    description varchar(256) not null,
    state varchar(256) not null,
    progress int default 0,
    created_by varchar(256) not null,
    created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_at TIMESTAMP not null,
    started_on TIMESTAMP,
    ended_on TIMESTAMP,
    foreign key(created_by) references users(username)
);