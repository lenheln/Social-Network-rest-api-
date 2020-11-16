-- Создание таблицы регионов
CREATE TABLE IF NOT EXISTS regions
(
  id        SERIAL               PRIMARY KEY,
  name      VARCHAR(256)         NOT NULL
);