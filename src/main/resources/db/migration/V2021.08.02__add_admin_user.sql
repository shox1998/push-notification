-- Create admin and insert to Data Base
insert into users(username,password,role,email,first_name,last_name, deleted)
values('admin','$2a$10$7Pd3Cgk1S4Xu6lgoux2qN.cgR2obbx7V1.NwAS0THCOTIIsJV5H5O','ADMIN','admin@mail.com','Jon','Snow',false);