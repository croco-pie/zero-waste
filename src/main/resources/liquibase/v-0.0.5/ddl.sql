--liquibase formatted sql

--changeset crocopie:add_is_added_by_users_column_to_waste_table
ALTER TABLE waste ADD COLUMN is_added_by_users BOOLEAN default false;