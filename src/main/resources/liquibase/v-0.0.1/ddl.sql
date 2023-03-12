--liquibase formatted sql

--changeset crocopie:add_waste_table
CREATE TABLE waste (
    id UUID PRIMARY KEY,
    code_num INT NOT NULL,
    code_name VARCHAR(20),
    code_description TEXT NOT NULL,
    is_shown BOOLEAN NOT NULL DEFAULT FALSE
    );