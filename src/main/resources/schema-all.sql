CREATE DATABASE IF NOT EXISTS testpeople;

Drop Table IF EXISTS People;

Create Table People (
	id int primary key auto_increment,
	name	varchar(40),
    image		BLOB,
    b1			boolean,
    location	varchar(40)
);