DROP TABLE IF EXISTS user_tbl;
CREATE TABLE user_tbl AS SELECT * FROM CSVREAD('classpath:user.csv');