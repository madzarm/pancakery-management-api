


CREATE TABLE pancake_order
(
    id            INT AUTO_INCREMENT NOT NULL,
    description VARCHAR(255) NULL,
    order_time    date NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

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

CREATE TABLE pancake
(
    id               INT AUTO_INCREMENT NOT NULL,
    pancake_order_id INT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE pancake_ingredient
(
    ingredient_id INT NOT NULL,
    pancake_id    INT NOT NULL
);



CREATE TABLE user
(
    id        INT AUTO_INCREMENT NOT NULL,
    active    BIT(1) NOT NULL,
    password  VARCHAR(255) NULL,
    role    VARCHAR(255) NULL,
    user_name VARCHAR(255) NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

ALTER TABLE pancake_ingredient
    ADD CONSTRAINT FK1atdvur5mwg3f5h3qfwswu7b3 FOREIGN KEY (pancake_id) REFERENCES pancake (id) ON UPDATE RESTRICT ON DELETE RESTRICT;
CREATE INDEX FK1atdvur5mwg3f5h3qfwswu7b3 ON pancake_ingredient (pancake_id);

ALTER TABLE pancake
    ADD CONSTRAINT FKifix6dlumaqt6fsv83kc7yeh8 FOREIGN KEY (pancake_order_id) REFERENCES pancake_order (id) ON UPDATE RESTRICT ON DELETE RESTRICT;
CREATE INDEX FKifix6dlumaqt6fsv83kc7yeh8 ON pancake (pancake_order_id);

ALTER TABLE pancake_ingredient
    ADD CONSTRAINT FKipvxjg6hrss5006hsv8uckuqc FOREIGN KEY (ingredient_id) REFERENCES ingredient (id) ON UPDATE RESTRICT ON DELETE RESTRICT;
CREATE INDEX FKipvxjg6hrss5006hsv8uckuqc ON pancake_ingredient (ingredient_id);


INSERT INTO ingredient (id,healthy,name,price,type) VALUES (1,false,'Maple Syrup',5.6,'DRESSING');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (2,true,'Fresh Fruit',3.6,'FRUIT');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (3,false,'Whipped Cream',4,'STUFFING');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (4,true,'Fresh Berries',5,'FRUIT');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (5,true,'Bananas and Chocolate Chips',7,'STUFFING');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (6,false,'Nutella',10,'BASE');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (7,true,'Blueberry Sauce',4,'STUFFING');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (8,true,'Nuts',4,'STUFFING');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (9,false,'Ice Cream',3,'STUFFING');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (10,true,'Fruit Compote',7.5,'FRUIT');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (11,true,'Blueberry Syrup',7.5,'FRUIT');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (12,false,'Caramel Sauce',8.5,'DRESSING');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (13,true,'Jams',5,'DRESSING');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (14,false,'Peanut Butter',5,'BASE');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (15,false,'Bacon',10,'BASE');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (16,false,'Apple Butter',11,'BASE');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (17,false,'Sausage',15,'BASE');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (18,true,'Fried Apples',15,'DRESSING');
INSERT INTO ingredient (id,healthy,name,price,type) VALUES (19,false,'Burger Patty',15,'STUFFING');

INSERT INTO pancake_order (`id`,`description`,`order_time`) VALUES (2,'desc','2022-04-01');
INSERT INTO pancake_order (`id`,`description`,`order_time`) VALUES (3,'desc','2022-04-01');
INSERT INTO pancake_order (`id`,`description`,`order_time`) VALUES (4,'desc','2022-03-27');
INSERT INTO pancake_order (`id`,`description`,`order_time`) VALUES (5,'desc','2022-03-12');
INSERT INTO pancake_order (`id`,`description`,`order_time`) VALUES (6,'desc','2022-02-12');

INSERT INTO `pancake` (id,`pancake_order_id`) VALUES (1,2);
INSERT INTO `pancake` (`id`,`pancake_order_id`) VALUES (2,2);
INSERT INTO `pancake` (`id`,`pancake_order_id`) VALUES (4,2);
INSERT INTO `pancake` (`id`,`pancake_order_id`) VALUES (3,3);
INSERT INTO `pancake` (`id`,`pancake_order_id`) VALUES (5,4);
INSERT INTO `pancake` (`id`,`pancake_order_id`) VALUES (6,4);
INSERT INTO `pancake` (`id`,`pancake_order_id`) VALUES (7,4);
INSERT INTO `pancake` (`id`,`pancake_order_id`) VALUES (8,5);
INSERT INTO `pancake` (`id`,`pancake_order_id`) VALUES (9,5);
INSERT INTO `pancake` (`id`,`pancake_order_id`) VALUES (10,5);
INSERT INTO `pancake` (`id`,`pancake_order_id`) VALUES (11,6);
INSERT INTO `pancake` (`id`,`pancake_order_id`) VALUES (12,6);

INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (11,1);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (6,1);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (6,2);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (6,3);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (6,6);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (6,7);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (4,2);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (4,3);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (4,6);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (4,7);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (4,8);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (4,9);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (4,10);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (17,4);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (17,5);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (17,8);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (17,9);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (17,10);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (3,2);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (3,3);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (3,6);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (3,11);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (5,2);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (5,3);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (5,6);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (5,7);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (5,8);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (5,9);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (5,10);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (5,11);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (8,1);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (8,3);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (8,6);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (8,7);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (8,11);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (1,2);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (1,3);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (1,6);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (1,7);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (1,8);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (1,9);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (1,10);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (1,11);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (1,12);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (2,2);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (2,3);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (2,6);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (2,7);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (2,8);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (2,9);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (2,10);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (2,12);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (7,1);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (7,3);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (7,6);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (7,7);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (7,8);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (7,9);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (7,10);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (7,12);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (9,1);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (9,3);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (9,12);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (10,1);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (10,4);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (10,5);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (10,12);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (15,11);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (15,12);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (18,4);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (18,5);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (18,7);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (18,8);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (18,9);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (18,10);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (18,11);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (18,12);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (19,4);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (19,5);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (19,7);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (19,8);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (19,9);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (19,10);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (19,11);
INSERT INTO `pancake_ingredient` (`ingredient_id`,`pancake_id`) VALUES (19,12);

INSERT INTO `user` (`id`,`active`,`password`,`role`,`user_name`) VALUES (1,true,'$2a$10$x077ymHqLWpXzStj/IC1guUhBhwpjhwsyyylLpJ5qX1G/9rl3j55q','ROLE_CUSTOMER','customer');
INSERT INTO `user` (`id`,`active`,`password`,`role`,`user_name`) VALUES (2,true,'$2a$10$Jaz1knk9kS1zVo6qpfDl6.HUC2MIzH1f2E4Usx9g4/5pEKHRx.Flq','ROLE_EMPLOYEE','employee');
INSERT INTO `user` (`id`,`active`,`password`,`role`,`user_name`) VALUES (3,true,'$2a$10$GtkUN8QU1z2Jiw4RBuE1g.UTky0S1/DyF1bVAuNwGx1vniimkT91O','ROLE_STOREOWNER','owner');
INSERT INTO `user` (`id`,`active`,`password`,`role`,`user_name`) VALUES (4,true,'$2a$10$xDxqC6R0.Ljq60B/5vfjieD.n6n02wXv4xxJU4X.HxyTqHQVbDjNG','ROLE_ADMIN','admin');


