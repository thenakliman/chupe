create table answers (
    id serial primary key,
    question_id int,
    answer varchar(1000),
    answered_by varchar(256) not null,
    created_at TIMESTAMP default CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    foreign key(answered_by) references users(username),
    foreign key(question_id) references questions(id)
);