DROP TABLE if EXISTS city;
CREATE TABLE IF NOT EXISTS city
(
    id   INT(11)      NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE if EXISTS district;
CREATE TABLE IF NOT EXISTS district
(
    id      INT(11)      NOT NULL AUTO_INCREMENT,
    name    VARCHAR(100) NOT NULL,
    city_id INT(11)      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_district_01 FOREIGN KEY (city_id) REFERENCES city (id) on delete no action on update no action
);