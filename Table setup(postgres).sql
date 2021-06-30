drop table events;
CREATE TABLE public.events (
	event_id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
	owner_id int4 not null,
	event_date int8 not null,
	city varchar(50) not null,
	state varchar(50) not null,
	description varchar(200) not null,
	skill_level varchar(50) not null,
	event_title varchar(50) not null,
	event_type varchar(50) not null,
	max_players int4 not null,
	CONSTRAINT event_pkey PRIMARY KEY (event_id),
	check (event_date > 0),
	check (max_players > 0)
	);
	
select * from events;

create table player (
	player_id int primary key generated always as identity,
	first_name varchar(50),
	last_name varchar(50),
	username varchar(25) unique,
	player_password varchar(25),
	bio varchar(400),
	visible boolean,
	email varchar(50),
	phone_number varchar(20),
	state varchar(25),
	city varchar(25)
);

drop table player;

create table registration (
	registration_id int primary key generated always as identity,
	player_id int,
	event_id int,
	constraint fk_player_id foreign key (player_id) references player(player_id) on delete cascade,
	constraint fk_event_id foreign key (event_id) references events(event_id) on delete cascade
);

drop table registration;