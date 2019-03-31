create table feedback_session (
    id serial primary key,
    description varchar(256) not null,
    created_by varchar(256) not null,
    created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_at TIMESTAMP not null,
    foreign key(created_by) references users(username)
);

create table feedback_point (
    id serial primary key,
    description varchar(256) not null,
    given_to varchar(256) not null,
    given_by varchar(256) not null,
    type varchar(256),
    created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_at TIMESTAMP not null,
    foreign key(given_by) references users(username),
    foreign key(given_by) references users(username)
);