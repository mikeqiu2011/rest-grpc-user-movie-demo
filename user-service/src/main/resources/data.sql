DROP TABLE IF EXISTS userentity;
CREATE TABLE userentity AS SELECT * FROM CSVREAD('classpath:user.csv');