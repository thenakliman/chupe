create table fund_type (
    id serial primary key,
    type varchar(20),
    default_amount int,
    description varchar(256) not null,
    created_by varchar(256),
    created_at TIMESTAMP default CURRENT_TIMESTAMP,
    updated_at TIMESTAMP default CURRENT_TIMESTAMP,
    foreign key(created_by) references users(username)
);

create table fund (
    id serial primary key,
    type int,
    owner varchar(256) not null,
    amount int not null,
    added_by varchar(256) not null,
    transaction_type varchar(10),
    created_at TIMESTAMP default CURRENT_TIMESTAMP,
    updated_at TIMESTAMP default CURRENT_TIMESTAMP,
    is_approved boolean default false,
    foreign key(added_by) references users(username),
    foreign key(owner) references users(username),
    foreign key(type) references fund_type(id)
);
