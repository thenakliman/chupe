create table questions (
    id serial primary key,
    question varchar(100) not null,
    description varchar(1000),
    created_at TIMESTAMP default CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    owner varchar(50) not null,
    assignedTO varchar(50) not null,
    foreign key(owner) references users(username),
    foreign key(assignedTO) references users(username)
);
