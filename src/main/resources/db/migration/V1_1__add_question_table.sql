create table questions (
    id serial primary key,
    question varchar(100) not null,
    description varchar(1000),
    created_at TIMESTAMP default CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    owner varchar(256) not null,
    assigned_to varchar(256) not null,
    foreign key(owner) references users(username),
    foreign key(assigned_to) references users(username)
);
