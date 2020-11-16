-- Создание таблицы Пользователей
CREATE TABLE IF NOT EXISTS users
(
  id                SERIAL              PRIMARY KEY,
  name              VARCHAR(45)         NOT NULL,
  surname           VARCHAR(45)         NOT NULL,
  date_of_birth     DATE                ,
  gender            CHAR(1)             ,
  interests         VARCHAR(512)        ,
  city_id           INTEGER             REFERENCES cities (id)
);

-- Создание индекса для таблицы пользователей
--CREATE INDEX filter_idx
--    ON users USING btree
--    (name, surname, age, gender, cityid);