create table questions (
    id int NOT NULL AUTO_INCREMENT primary key,
    question varchar(100) NOT NULL,
    description varchar(1000),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    owner varchar(50),
    assignedTO varchar(50),
    foreign key(owner) references users(username),
    foreign key(assignedTO) references users(username)
);
