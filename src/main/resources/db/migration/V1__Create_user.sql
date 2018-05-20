create table users(
    first_name varchar(50),
    last_name varchar(50),
	username varchar(50) not null primary key,
	email varchar(50),
	password varchar(1024) not null,
	enabled boolean not null
);

create table authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username, authority);

insert into users values('Prasun', 'hazari', 'prasun_hazari', 'phazari@example.com', '$2a$04$Z8Rsh/NmQ/sdTPrcig00j.A3mZADzreAVoQx7ea9sD4rPwZRpPnMG', True);
insert into users values('Lal', 'Singh', 'lal_singh', 'lalsingh@example.com', '$2a$04$Z8Rsh/NmQ/sdTPrcig00j.A3mZADzreAVoQx7ea9sD4rPwZRpPnMG', True);
insert into users values('Kulamani', 'Sethi', 'kulamani_sethi', 'ksethi@example.com', '$2a$04$Z8Rsh/NmQ/sdTPrcig00j.A3mZADzreAVoQx7ea9sD4rPwZRpPnMG', True);
insert into users values('Prince', 'Bhatia', 'prince_bhatia', 'prince_bhatia@example.com', '$2a$04$Z8Rsh/NmQ/sdTPrcig00j.A3mZADzreAVoQx7ea9sD4rPwZRpPnMG', True);

insert into authorities values('prasun_hazari', 'admin');
insert into authorities values('lal_singh', 'member');
insert into authorities values('kulamani_sethi', 'member');
insert into authorities values('prince_bhatia', 'member');
