CREATE TABLE service (
    id INT AUTO_INCREMENT NOT NULL,
    title VARCHAR(128) NOT NULL,
    cost INT DEFAULT 0,
    duration_days int DEFAULT 0,
    PRIMARY KEY (id)
    );

CREATE TABLE customer (
    id INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(128) NOT NULL,
    age INT,
    phone VARCHAR(15),
    city VARCHAR(128),
    street VARCHAR(128),
    PRIMARY KEY (id)
    );

CREATE TABLE subscription(
    id INT NOT NULL AUTO_INCREMENT,
    c_id INT NOT NULL,
    s_id INT NOT NULL,
    beginning_date DATE DEFAULT CURDATE(),
    is_active BOOL DEFAULT FALSE,
    INDEX sub_id (c_id),
    INDEX ser_id (s_id),
    PRIMARY KEY (id),
    FOREIGN KEY (c_id)
        REFERENCES customer (id)
        ON DELETE CASCADE,
    FOREIGN KEY (s_id)
        REFERENCES service(id)
        ON DELETE CASCADE
);