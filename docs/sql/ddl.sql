-- Generated 2024-03-12 15:28:14-0600 for database version 1

CREATE TABLE IF NOT EXISTS `user`
(
    `user_id`      INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `oauth_key`    TEXT,
    `display_name` TEXT COLLATE NOCASE
);

CREATE UNIQUE INDEX IF NOT EXISTS `index_user_oauth_key` ON `user` (`oauth_key`);

CREATE UNIQUE INDEX IF NOT EXISTS `index_user_display_name` ON `user` (`display_name`);

CREATE TABLE IF NOT EXISTS `game`
(
    `game_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `key`     TEXT,
    `pool`    TEXT,
    `length`  INTEGER                           NOT NULL,
    `start`   INTEGER,
    `user_id` INTEGER                           NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS `index_game_key` ON `game` (`key`);

CREATE INDEX IF NOT EXISTS `index_game_length` ON `game` (`length`);

CREATE INDEX IF NOT EXISTS `index_game_user_id` ON `game` (`user_id`);

CREATE TABLE IF NOT EXISTS `guess`
(
    `guess_id`  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `key`       TEXT,
    `content`   TEXT,
    `correct`   INTEGER                           NOT NULL,
    `close`     INTEGER                           NOT NULL,
    `timestamp` INTEGER,
    `game_id`   INTEGER                           NOT NULL,
    FOREIGN KEY (`game_id`) REFERENCES `game` (`game_id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS `index_guess_key` ON `guess` (`key`);

CREATE INDEX IF NOT EXISTS `index_guess_game_id_timestamp` ON `guess` (`game_id`, `timestamp`);

CREATE INDEX IF NOT EXISTS `index_guess_game_id` ON `guess` (`game_id`);

CREATE VIEW `game_result` AS
SELECT gm.game_id                            AS game_result_id,
       gm.user_id,
       gm.length                             AS code_length,
       COUNT(*)                              as guess_count,
       MAX(gs.timestamp) - MIN(gs.timestamp) AS duration,
       MAX(gs.timestamp)                     AS timestamp
FROM game AS gm
         JOIN guess AS gs
              ON gs.game_id = gm.game_id
GROUP BY gm.game_id;