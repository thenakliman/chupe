create table retrospection_point (
    id serial primary key,
    description varchar(256) not null,
    added_by varchar(256) not null,
    type varchar(256),
    created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_at TIMESTAMP not null,
    foreign key(added_by) references users(username)
);

create table retrospection_vote (
    id serial primary key,
    voted_by varchar(256) not null,
    up_vote boolean,
    retrospection int,
    created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_at TIMESTAMP not null,
    foreign key(voted_by) references users(username),
    foreign key(retrospection) references retrospection_point(id)
);
