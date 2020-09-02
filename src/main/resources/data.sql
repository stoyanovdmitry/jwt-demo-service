insert into users (id, username, password) values (1, 'Diego', 'pass');
insert into users (id, username, password) values (2, 'string', 'string');
insert into users (id, username, password) values (3, 'admin', '1');

insert into roles (id, rolename) values (1, 'USER');
insert into roles (id, rolename) values (2, 'ADMIN');

insert into user_role (user_id, role_id) values (1, 1);
insert into user_role (user_id, role_id) values (2, 1);
insert into user_role (user_id, role_id) values (3, 1);
insert into user_role (user_id, role_id) values (3, 2);
