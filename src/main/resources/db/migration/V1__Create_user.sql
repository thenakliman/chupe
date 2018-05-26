create table users(
    first_name varchar(256),
    last_name varchar(256),
	username varchar(256) not null primary key,
	email varchar(256),
	password varchar(2048) not null,
	enabled boolean not null
);

create table authorities (
    id serial primary key,
	username varchar(256) not null,
	authority varchar(256) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username, authority);

insert into users (first_name, last_name, username, email, password, enabled)
            values('Prasun', 'hazari', 'prasun_hazari', 'phazari@example.com', '$2a$04$Z8Rsh/NmQ/sdTPrcig00j.A3mZADzreAVoQx7ea9sD4rPwZRpPnMG', 'true');
insert into users (first_name, last_name, username, email, password, enabled)
            values('Lal', 'Singh', 'lal_singh', 'lalsingh@example.com', '$2a$04$Z8Rsh/NmQ/sdTPrcig00j.A3mZADzreAVoQx7ea9sD4rPwZRpPnMG', 'true');
insert into users (first_name, last_name, username, email, password, enabled)
            values('Kulamani', 'Sethi', 'kulamani_sethi', 'ksethi@example.com', '$2a$04$Z8Rsh/NmQ/sdTPrcig00j.A3mZADzreAVoQx7ea9sD4rPwZRpPnMG', 'true');
insert into users (first_name, last_name, username, email, password, enabled)
            values('Prince', 'Bhatia', 'prince_bhatia', 'prince_bhatia@example.com', '$2a$04$Z8Rsh/NmQ/sdTPrcig00j.A3mZADzreAVoQx7ea9sD4rPwZRpPnMG', 'true');

insert into authorities (username, authority) values('prasun_hazari', 'admin');
insert into authorities (username, authority) values('lal_singh', 'member');
insert into authorities (username, authority) values('kulamani_sethi', 'member');
insert into authorities (username, authority) values('prince_bhatia', 'member');
