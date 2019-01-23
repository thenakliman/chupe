create table retrospection (
    id serial primary key,
    name varchar(256) not null,
    created_by varchar(256) not null,
    maximum_vote int not null,
    created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_at TIMESTAMP not null,
    foreign key(created_by) references users(username)
);