CREATE TABLE `users`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `username`   VARCHAR(255) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `password`   VARCHAR(512) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `role`       ENUM('ROLE_USER','ROLE_ADMIN') NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `created_at` DATETIME NULL DEFAULT (CURRENT_TIMESTAMP),
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `username` (`username`) USING BTREE
);

CREATE TABLE `refresh_token`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `user_id`    BIGINT       NOT NULL,
    `token`      VARCHAR(512) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `expires_at` DATETIME     NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `token` (`token`) USING BTREE,
    INDEX `user_id` (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `movies`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `title`      VARCHAR(255) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `year`       VARCHAR(10) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `poster`     VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `plot`       TEXT NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `genre`      VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `imdb_rating` VARCHAR(10) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `runtime`    VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
    PRIMARY KEY (`id`) USING BTREE
);

CREATE TABLE `film_session`
(
    `id`         BIGINT         NOT NULL AUTO_INCREMENT,
    `movie_id`   BIGINT         NOT NULL,
    `price`      DECIMAL(10, 2) NOT NULL,
    `date`       DATE           NOT NULL,
    `start_time` TIME           NOT NULL,
    `end_time`   TIME           NOT NULL,
    `capacity`   INT            NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE `ticket`
(
    `id`            BIGINT      NOT NULL AUTO_INCREMENT,
    `user_id`       BIGINT      NOT NULL,
    `session_id`    BIGINT      NOT NULL,
    `seat_number`   VARCHAR(10) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `purchase_time` DATETIME NULL DEFAULT (CURRENT_TIMESTAMP),
    `status`        ENUM('PENDING','CONFIRMED','CANCELLED','RETURNED') NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `request_type`  ENUM('PURCHASE','RETURN') NOT NULL DEFAULT 'PURCHASE' COLLATE 'utf8mb4_0900_ai_ci',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX           `fk_ticket_user` (`user_id`) USING BTREE,
    INDEX           `fk_ticket_session` (`session_id`) USING BTREE,
    CONSTRAINT `fk_ticket_session` FOREIGN KEY (`session_id`) REFERENCES `film_session` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `fk_ticket_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
);
