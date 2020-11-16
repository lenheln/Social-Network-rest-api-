INSERT INTO users (name, surname, dateOfBDay, gender, cityId, interests) VALUES ('Маша', 'Иванова', '1987-07-21', 'F', 1, 'books');
INSERT INTO users (name, surname, dateOfBDay, gender, cityId, interests) VALUES ('Пётр', 'Петров', '1955-02-21', 'M', 2, 'music');
INSERT INTO users (name, surname, dateOfBDay, gender, cityId, interests) VALUES ('Олег', 'Олегов', '1980-05-03', 'M', 3, 'sport');

insert into friendship (iduser, idfriend) VALUES (1,2);
insert into friendship (iduser, idfriend) VALUES (3,1);