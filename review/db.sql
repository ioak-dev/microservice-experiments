CREATE DATABASE reviews;

CREATE TABLE Review (
                      id VARCHAR(255) PRIMARY KEY,
                      product_id VARCHAR(255),
                      user_id VARCHAR(255),
                      review_description TEXT,
                      ratings INT
);
