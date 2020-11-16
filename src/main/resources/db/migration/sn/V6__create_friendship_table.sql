--Создание таблицы описывающей дружбу пользователей
CREATE TABLE IF NOT EXISTS friendship
(
    iduser      INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    idfriend    INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    PRIMARY KEY (idUser, idFriend)
);