--liquibase formatted sql
--changeset crocopie:add_waste_type_enum

CREATE TYPE WASTE_TYPE_ENUM AS ENUM ('PLASTIC', 'METAL', 'PAPER', 'GLASS', 'COMPOSITE', 'OTHER');

ALTER TABLE zero_waste.waste ADD COLUMN waste_type WASTE_TYPE_ENUM;