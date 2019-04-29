create table meeting (
    id serial primary key,
    subject varchar(256) not null,
    created_by varchar(256) not null,
    created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_at TIMESTAMP not null,
    foreign key(created_by) references users(username)
);

create table meeting_discussion_item (
    id serial primary key,
    meeting_id int not null,
    discussion_item varchar(256) not null,
    type varchar(256),
    assigned_to varchar(256) not null,
    created_by varchar(256) not null,
    created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
    updated_at TIMESTAMP not null,
    foreign key(created_by) references users(username),
    foreign key(assigned_to) references users(username),
    foreign key(meeting_id) references meeting(id)
);