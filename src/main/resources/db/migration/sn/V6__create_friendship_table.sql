--Создание таблицы описывающей дружбу пользователей
CREATE TABLE IF NOT EXISTS friendship
(
    id_user      INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    id_friend    INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    PRIMARY KEY (id_user, id_friend)
);