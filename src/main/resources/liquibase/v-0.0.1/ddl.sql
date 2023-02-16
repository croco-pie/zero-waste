--liquibase formatted sql

--changeset crocopie:add_waste_table
CREATE TABLE zero_waste.waste (
    id UUID PRIMARY KEY,
    code_num INT NOT NULL,
    code_name VARCHAR(20),
    code_description TEXT NOT NULL,
    is_shown BOOLEAN NOT NULL DEFAULT FALSE
    );