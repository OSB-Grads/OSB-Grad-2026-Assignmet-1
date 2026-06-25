INSERT into PRODUCTS VALUES (001 , "Bonus Saver", "Savings" , 2.00, 5000, null);
INSERT into PRODUCTS VALUES (002 , "SA1", "Savings" , 2.50, 5000, null);
INSERT into PRODUCTS VALUES (003 , "FD1", "Fixed Deposits" , 2.00, 5000, 12);
INSERT into PRODUCTS VALUES (004 , "FD2", "Fixed Deposits" , 3.00, 6000, 36);
INSERT into PRODUCTS VALUES (005 , "LA1", "Limited Access" , 5.00, 5000, null);
INSERT into PRODUCTS VALUES (006 , "SA2", "Savings" , 3.00, 6000, null);
INSERT into PRODUCTS VALUES (007 , "LA2", "Limited Access" , 5.00, 5000, null);
INSERT into PRODUCTS VALUES (008 , "FD3", "Fixed Deposits" , 3.50, 7000, 24);
INSERT into PRODUCTS VALUES (009 , "LA3", "Limited Access" , 2.00, 5000, null);
INSERT into PRODUCTS VALUES (010 , "SA3", "Savings" , 6.00, 4500, null);
INSERT into PRODUCTS VALUES (011 , "FD4", "Fixed Deposits" , 4.00, 2500, 60);

INSERT INTO customers (id, first_name, last_name, date_of_birth, email, phone, address, national_id)
VALUES (0,"Akash","Ch","2000-11-24","Akash@x.com","1234567890","ABC","123456712345");

INSERT INTO auth (id, username, password_hash, role)
VALUES (0,"Admin","Admin@123","ADMIN");


INSERT INTO accounts (id,account_number, customer_id, product_id, balance)
VALUES ("a0d4e087-5c18-41az-87a8-94c2d6337111","OSBABABCZI20226","be9f108c-ca12-48fb-bc80-f4623a7e02aa",8,5000.00);
INSERT INTO accounts (id,account_number, customer_id, product_id, balance)
VALUES ("n8d4e087-r2mz-41az-87a8-94c2d6337530","OSBABABVCX2026","be9f108c-ca12-48fb-bc80-f4623a7e02aa",7,10000.00);
INSERT INTO accounts (id,account_number, customer_id, product_id, balance)
VALUES ("b4d4e098-5c18-41az-87a8-94c2d6336850","OSBABARNZI2026","be9f108c-ca12-48fb-bc80-f4623a7e02aa",10,3000.00);
INSERT INTO accounts (id,account_number, customer_id, product_id, balance)
VALUES ("a4r8e087-5f48-41az-87a8-94c2d6337280","OSBABFCII2026","be9f108c-ca12-48fb-bc80-f4623a7e02aa",3,6000.00);
INSERT INTO accounts (id,account_number, customer_id, product_id, balance)
VALUES ("a6y0e087-5g58-41az-87a8-94c2d6337111","OSBABWDFYI2026","be9f108c-ca12-48fb-bc80-f4623a7e02aa",5,7500.00);
INSERT INTO accounts (id,account_number, customer_id, product_id, balance)
VALUES ("a2m7e087-5h68-41az-87a8-94c2d6356111","OSBABMNOZI2026","be9f108c-ca12-48fb-bc80-f4623a7e02aa",1,9000.00);

delete from accounts where customer_id = "90f75de6-5c74-4aa0-bedc-89db9f3487fd";

UPDATE auth
SET role = 'ADMIN'
WHERE id = 'd8770da3-afbf-4c86-b7eb-20171658ab5d';


SELECT account_number, balance
FROM accounts
WHERE account_number='OSBABMNOZI2026';

select * from inbox where id = 12;

drop table inbox;


INSERT INTO inbox (id, correlation_id, transaction_id, message_type, payload, status, reason)
VALUES ('6c079311-a518-4ee8-926a-36731758a981',126,'a95a6982-9517-416c-a91a-76265a4e3807','DEPOSITS','{"account_number":"OSBABMNOZI2026","amount":3000.00}','PENDING','Deposit 2000');