CREATE TABLE service (
    id INT AUTO_INCREMENT NOT NULL,
    title VARCHAR(128) NOT NULL,
    cost INT DEFAULT 0,
    duration_days int DEFAULT 0,
    PRIMARY KEY (id)
    );

CREATE TABLE subscriber (
    id INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(128) NOT NULL,
    age INT,
    phone VARCHAR(15),
    city VARCHAR(128),
    street VARCHAR(128),
    PRIMARY KEY (id)
    );

CREATE TABLE subscriptions(
    sub_id INT NOT NULL,
    ser_id INT NOT NULL,
    beginning_date DATE NOT NULL DEFAULT CURDATE(),
    is_active BOOL NOT NULL DEFAULT FALSE,
    INDEX sub_id (sub_id),
    INDEX ser_id (ser_id),
    PRIMARY KEY (sub_id,ser_id),
    FOREIGN KEY (sub_id)
        REFERENCES subscriber(id)
        ON DELETE CASCADE,
    FOREIGN KEY (ser_id)
        REFERENCES service(id)
        ON DELETE CASCADE
)