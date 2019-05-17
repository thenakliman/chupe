create table retro_action_item (
    id serial primary key,
    description varchar(2000) not null,
    retro_id int not null,
    retro_point_id int,
    status varchar(256) not null,
    last_date_to_act TIMESTAMP,
    created_by varchar(256) not null,
    assigned_to varchar(256) not null,
    created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_at TIMESTAMP not null,
    foreign key(created_by) references users(username),
    foreign key(assigned_to) references users(username),
    foreign key(retro_id) references retrospection(id),
    foreign key(retro_point_id) references retrospection_point(id)
);