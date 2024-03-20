-- CREATE TABLE users (
--                        id int AUTO_INCREMENT PRIMARY KEY,
--                        username VARCHAR(255),
--                        password VARCHAR(255),
--                        email VARCHAR(255),
--                        role VARCHAR(255)
-- );
--
--
-- CREATE TABLE project (
--                           id int AUTO_INCREMENT PRIMARY KEY,
--                           name VARCHAR(255),
--                           description VARCHAR(255),
--                           created_date DATE
-- );


create table if not exists Person
(
    id       int AUTO_INCREMENT PRIMARY KEY,
    name     varchar(20)        not null,
    age      int,
    card     bigint UNIQUE,
    phone    bigint UNIQUE         not null,
    email    varchar(30) UNIQUE not null,
    role     varchar(20)        not null,
    password varchar
);

create table if not exists Seller
(
    id      int AUTO_INCREMENT PRIMARY KEY,
    company varchar(20)        not null,
    card    bigint UNIQUE,
    phone   bigint UNIQUE         not null,
    email   varchar(30) UNIQUE not null,
    role    varchar(20)        not null
);

create table if not exists Coupon
(
    id       int AUTO_INCREMENT PRIMARY KEY,
    name     varchar(20),
    discount int,
    valid    boolean,
    creat_at timestamp
);

create table if not exists Orders
(
    id        int AUTO_INCREMENT PRIMARY KEY,
    person_id int references person (id) not null,
    coupon_id int references coupon (id),
    creat_at  timestamp,
    status    varchar
);

CREATE TABLE IF NOT EXISTS Car
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    maker     VARCHAR(30) NOT NULL,
    model     VARCHAR(30) NOT NULL,
    prod_year INT         NOT NULL,
    typecar   VARCHAR(255),
    gearbox   VARCHAR(255),
    engine    DECIMAL,
    fuel      VARCHAR(255),
    colour    VARCHAR(255),
    img       VARCHAR(200),
    price     DECIMAL,
    volume    INT,
    purchased BOOLEAN,
    orders_id INT,
    FOREIGN KEY (orders_id) REFERENCES Orders (id)
);

create table if not exists Orders_Car
(
    orders_id int references Orders (id),
    car_id    int references Car (id),
    lot       int check (lot > 0),
    primary key (orders_id, car_id)
);
