--liquibase formatted sql
--changeset crocopie:add_enum_values_to_waste

UPDATE zero_waste.waste SET waste_type = 'PLASTIC' WHERE code_num BETWEEN '1' AND '19';
UPDATE zero_waste.waste SET waste_type = 'PAPER' WHERE code_num BETWEEN '20' AND '39';
UPDATE zero_waste.waste SET waste_type = 'METAL' WHERE code_num BETWEEN '40' AND '49';
UPDATE zero_waste.waste SET waste_type = 'GLASS' WHERE code_num BETWEEN '70' AND '79';
UPDATE zero_waste.waste SET waste_type = 'COMPOSITE' WHERE code_num BETWEEN '80' AND '99';
