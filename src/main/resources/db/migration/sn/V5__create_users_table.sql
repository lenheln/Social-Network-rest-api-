-- Создание таблицы Пользователей
CREATE TABLE IF NOT EXISTS users
(
  id            SERIAL              PRIMARY KEY,
  name          VARCHAR(45)         NOT NULL,
  surname       VARCHAR(45)         NOT NULL,
  dateOfBDay    DATE                ,
  gender        CHAR(1)             ,
  interests     VARCHAR(512)        ,
  cityId        INTEGER             REFERENCES cities (id)
);

-- Создание индекса для таблицы пользователей
--CREATE INDEX filter_idx
--    ON users USING btree
--    (name, surname, age, gender, cityid);