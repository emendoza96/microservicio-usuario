INSERT INTO construction_type (id, type) VALUES (1, 'REPAIR');
INSERT INTO construction_type (id, type) VALUES (2, 'HOUSE');
INSERT INTO construction_type (id, type) VALUES (3, 'BUILDING');
INSERT INTO construction_type (id, type) VALUES (4, 'ROAD');

INSERT INTO role (id, type) VALUES (1, 'CUSTOMER');
INSERT INTO role (id, type) VALUES (2, 'SELLER');

INSERT INTO user_entity (id, username, password, role_id) VALUES (1000, 'emi123', '$2a$10$3MqlE56NbZRMPDduskqlV.j5UepDJn/NJcvzgaLvaUO76u/.ar65e', 1);
