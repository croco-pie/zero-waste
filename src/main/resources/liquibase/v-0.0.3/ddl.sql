--liquibase formatted sql

--changeset crocopie:add_waste_type_enum
CREATE TYPE WASTE_TYPE AS ENUM ('OTHER', 'PLASTIC', 'PAPER', 'METAL', 'TIMBER', 'TEXTILE', 'GLASS', 'COMPOSITE');

--changeset crocopie:alter_waste_table_add_waste_type_enum
ALTER TABLE zero_waste.waste ADD COLUMN waste_type WASTE_TYPE;
