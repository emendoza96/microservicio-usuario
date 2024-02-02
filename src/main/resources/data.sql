INSERT INTO construction_type (id, type) VALUES (1, 'REPAIR');
INSERT INTO construction_type (id, type) VALUES (2, 'HOUSE');
INSERT INTO construction_type (id, type) VALUES (3, 'BUILDING');
INSERT INTO construction_type (id, type) VALUES (4, 'ROAD');

INSERT INTO role (id, type) VALUES (1, 'CUSTOMER');
INSERT INTO role (id, type) VALUES (2, 'SELLER');

INSERT INTO user (id, username, password, role_id) VALUES (1, 'emiElLoco', 'emi123', 1);
INSERT INTO user (id, username, password, role_id) VALUES (2, 'celi', 'celi123', 1);
INSERT INTO user (id, username, password, role_id) VALUES (3, 'test', 'test123', 2);

INSERT INTO employee (id, email, user_id) VALUES (1, 'emiliano@gmail.com', 1);
INSERT INTO employee (id, email, user_id) VALUES (2, 'test@gmail.com', 3);

INSERT INTO customer (id, business_name, cuit, email, user_id) VALUES (1, 'Celi la loca', '14311234', 'celi@gmail.com', 2);

INSERT INTO construction (id, description, latitude, longitude, direction, area, type_id, customer_id) VALUES (1, 'test', 34.2, 41.6, 'BS AS - 234', 54, 1, 1);
INSERT INTO construction (id, description, latitude, longitude, direction, area, type_id, customer_id) VALUES (2, 'test2', 44.2, 31.6, 'Santa Fe - 111', 20, 2, 1);