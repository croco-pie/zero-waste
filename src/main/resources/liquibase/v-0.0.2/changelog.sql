--liquibase formatted sql
--changeset crocopie:add_values_to_waste

INSERT INTO zero_waste.waste (id,code_num,code_name,code_id,code_description,is_shown) VALUES
	 ('60430885-cf94-460e-8902-9604c561f2aa','72','gl','0cab183e-ad6e-4af1-979b-d472b9fe58a1','Стекло',true),
	 ('798b6dd5-9bc7-479d-84db-7369fcdf71a1','73','gl','0cab183e-ad6e-4af1-979b-d472b9fe58a1','Стекло',true),
	 ('0e88af0d-e822-4ed0-b8f9-0cae23deed3a','74','gl','0cab183e-ad6e-4af1-979b-d472b9fe58a1','Стекло',true),
	 ('c21b7df1-bda6-4445-9285-75fff47aab5b','7','other','6934e339-6235-48b8-92e8-2b24db646c15','Смешанный пластик',true),
	 ('7327841f-99bd-4149-9c23-c93fa6c8cf9d','81','tetrapak','ecc24f55-41d0-4d61-9d47-ad82d8636de8','Многослойная упаковка для соков, молока и т.д.',true),
	 ('b333754a-fcb3-4a61-968d-8296ac3da1d3','1','pet','5f051b81-9331-46cf-ad45-6cb7f334d340','Бутылочный пластик, пластик для контейнеров, пленка',true),
	 ('e6531a94-74be-4b70-b969-c49b16b227cb','41','alu','608199a0-338c-4af9-93fe-ae636e16d240','Алюминий, банки и тюбики от крема',true),
	 ('19f778e5-9306-42bf-9634-56f714a8bfad','40','fe','9a4a7d0d-3cee-4867-a08b-edc426023f68','Жестяные банки из-под консервов',true),
	 ('1eca88e2-5f86-44a8-ae31-65fc841b1f11','70','gl','0cab183e-ad6e-4af1-979b-d472b9fe58a1','Стекло',true),
	 ('4be2e884-74e8-4fce-82a7-093ce1e493bc','0','','c110e9ae-5b8f-479d-9f04-a7164a441bcc','Неизвестно',false),
	 ('77fc9e5b-c5b0-4825-b237-dd1707be4301','82','tetrapak','ecc24f55-41d0-4d61-9d47-ad82d8636de8','Многослойная упаковка для соков, молока и т.д.',true),
	 ('9d3e8011-7795-4abc-9437-6944ce8f31a1','84','tetrapak','ecc24f55-41d0-4d61-9d47-ad82d8636de8','Многослойная упаковка для соков, молока и т.д.',true),
	 ('c77c4987-5d73-42af-b7ca-3c9a32c27bf0','71','gl','0cab183e-ad6e-4af1-979b-d472b9fe58a1','Стекло',true);



