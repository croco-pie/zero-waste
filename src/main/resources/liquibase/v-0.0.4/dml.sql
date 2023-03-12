--liquibase formatted sql

--changeset crocopie:add_waste_types
UPDATE waste SET waste_type = 'OTHER' WHERE code_num = '0';
UPDATE waste SET waste_type = 'PLASTIC' WHERE code_num BETWEEN '1' AND '19';
UPDATE waste SET waste_type = 'PAPER' WHERE code_num BETWEEN '20' AND '39';
UPDATE waste SET waste_type = 'METAL' WHERE code_num BETWEEN '40' AND '49';
UPDATE waste SET waste_type = 'GLASS' WHERE code_num BETWEEN '70' AND '79';
UPDATE waste SET waste_type = 'COMPOSITE' WHERE code_num BETWEEN '80' AND '99';
