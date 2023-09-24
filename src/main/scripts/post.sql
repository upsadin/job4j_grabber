CREATE TABLE if not exists post(
id serial primary key,
name varchar(50),
text varchar(250),
link varchar(200) UNIQUE,
created timestamp
);