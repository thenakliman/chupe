create table best_practice (
    id serial primary key,
    description varchar(2000) not null,
    applicable boolean not null,
    created_by varchar(256) not null,
    created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_at TIMESTAMP not null,
    foreign key(created_by) references users(username)
);

create table best_practice_assessment (
    id serial primary key,
    retrospection_id int not null,
    answered_by varchar(256) not null,
    created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_at TIMESTAMP not null,
    foreign key(answered_by) references users(username),
    foreign key(retrospection_id) references retrospection(id),
    CONSTRAINT UC_best_practice_assessment UNIQUE (retrospection_id, answered_by)
);

create table best_practices_assessment_answer (
    id serial primary key,
    best_practice_assessment_id int not null,
    best_practice_id int not null,
    answer boolean not null,
    created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_at TIMESTAMP not null,
    foreign key(best_practice_id) references best_practice(id),
    foreign key(best_practice_assessment_id) references best_practice_assessment(id)
);

