create table best_practice (
    id serial primary key,
    description varchar(2000) not null,
    need_improvement varchar(256) not null,
    done_well varchar(256) not null,
    applicable boolean not null,
    created_by varchar(256) not null,
    created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_at TIMESTAMP not null,
    foreign key(created_by) references users(username)
);

create table best_practice_assessment (
    id serial primary key,
    best_practice_id int not null,
    retrospection_id int not null,
    answer boolean not null,
    answered_by varchar(256) not null,
    created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_at TIMESTAMP not null,
    foreign key(answered_by) references users(username),
    foreign key(retrospection_id) references retrospection(id),
    foreign key(best_practice_id) references best_practice(id)
);

