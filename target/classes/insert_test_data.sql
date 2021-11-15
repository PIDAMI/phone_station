INSERT INTO customer (name, age, phone, city, street)
VALUES ('Ivan',20,'123123123','Saint Petersburg','Alekseevskaya Street'),
       ('Peter',34,NULL,'Moscow',NULL),
       ('Alexander',NULL,'67671324',NULL,NULL),
       ('Basil',46,'999888','Saint Petersburg','Ray Street');


INSERT INTO service (title, cost, duration_days)
VALUES ('weather forecast',300,40),
       ('news',225,15),
       ('e-library',500,10);

INSERT INTO subscription (c_id, s_id, beginning_date, is_active)
VALUES (1,1,'2020-10-01',TRUE),
       (1,3,'2012-07-12',FALSE),
       (2,1,'2021-11-01',TRUE),
       (3,2,'2019-12-12',TRUE),
       (3,3,'2017-10-02',FALSE);




