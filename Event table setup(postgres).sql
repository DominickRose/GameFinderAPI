
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