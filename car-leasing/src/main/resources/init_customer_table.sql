CREATE TABLE customer (
    id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    full_name VARCHAR(60) NOT NULL,
    phone VARCHAR(20),
    birth_date DATE,
    address VARCHAR(100)
 );