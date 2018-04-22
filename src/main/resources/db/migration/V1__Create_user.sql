create table users(
	username varchar(50) not null primary key,
	password varchar(50) not null,
	enabled boolean not null
);

create table authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username, authority);

insert into users values('user1', 'password1', True);
insert into users values('user2', 'password2', True);
insert into users values('user3', 'password3', True);
insert into users values('user4', 'password4', True);

insert into authorities values('user1', 'admin');
insert into authorities values('user3', 'admin');
insert into authorities values('user2', 'member');
insert into authorities values('user4', 'member');
