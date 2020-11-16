-- Создание таблицы городов
CREATE TABLE IF NOT EXISTS cities
(
  id        SERIAL              PRIMARY KEY,
  name      VARCHAR(256)        NOT NULL,
  regionId  INTEGER             REFERENCES regions (id)
);

-- Создание индекса на столбец с названием города
--CREATE INDEX cityName_idx
--    ON cities USING btree (name);