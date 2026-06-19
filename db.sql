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
