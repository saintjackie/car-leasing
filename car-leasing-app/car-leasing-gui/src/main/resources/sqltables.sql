CREATE TABLE LEASE (id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    carId BIGINT,
                    customerId BIGINT,
                    startTime TIMESTAMP,
                    expectedEndTime TIMESTAMP,
                    realEndTime TIMESTAMP,
                    price DECIMAL,
                    fee DECIMAL);
CREATE TABLE customer (id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    full_name VARCHAR(60) NOT NULL,
                    phone VARCHAR(20),
                    birth_date DATE,
                    address VARCHAR(100));
CREATE TABLE CAR (id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    type VARCHAR(50),
                    vendor VARCHAR(50),
                    modelYear INT,
                    seats INT,
                    registrationPlate VARCHAR(10));
