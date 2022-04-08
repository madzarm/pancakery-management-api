-- liquibase formatted sql

-- changeset madza:1649109243047-1
CREATE TABLE ingredient
(
    id      INT AUTO_INCREMENT NOT NULL,
    healthy BIT(1)       NOT NULL,
    name    VARCHAR(255) NOT NULL,
    price   FLOAT(12)    NOT NULL,
    type    VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id),
    UNIQUE (name)
);

-- changeset madza:1649109243047-2
CREATE TABLE pancake
(
    id               INT AUTO_INCREMENT NOT NULL,
    pancake_order_id INT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

-- changeset madza:1649109243047-3
CREATE TABLE pancake_ingredient
(
    ingredient_id INT NOT NULL,
    pancake_id    INT NOT NULL
);

-- changeset madza:1649109243047-4
CREATE TABLE pancake_order
(
    id            INT AUTO_INCREMENT NOT NULL,
    `description` VARCHAR(255) NULL,
    order_time    date NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

-- changeset madza:1649109243047-5
CREATE TABLE user
(
    id        INT AUTO_INCREMENT NOT NULL,
    active    BIT(1) NOT NULL,
    password  VARCHAR(255) NULL,
    `role`    VARCHAR(255) NULL,
    user_name VARCHAR(255) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

-- changeset madza:1649109243047-9
ALTER TABLE pancake_ingredient
    ADD CONSTRAINT FK1atdvur5mwg3f5h3qfwswu7b3 FOREIGN KEY (pancake_id) REFERENCES pancake (id) ON UPDATE RESTRICT ON DELETE RESTRICT;
CREATE INDEX FK1atdvur5mwg3f5h3qfwswu7b3 ON pancake_ingredient (pancake_id);

-- changeset madza:1649109243047-10
ALTER TABLE pancake
    ADD CONSTRAINT FKifix6dlumaqt6fsv83kc7yeh8 FOREIGN KEY (pancake_order_id) REFERENCES pancake_order (id) ON UPDATE RESTRICT ON DELETE RESTRICT;
CREATE INDEX FKifix6dlumaqt6fsv83kc7yeh8 ON pancake (pancake_order_id);

-- changeset madza:1649109243047-11
ALTER TABLE pancake_ingredient
    ADD CONSTRAINT FKipvxjg6hrss5006hsv8uckuqc FOREIGN KEY (ingredient_id) REFERENCES ingredient (id) ON UPDATE RESTRICT ON DELETE RESTRICT;
CREATE INDEX FKipvxjg6hrss5006hsv8uckuqc ON pancake_ingredient (ingredient_id);

