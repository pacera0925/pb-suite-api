INSERT INTO web_user (id, first_name, last_name, username, password) VALUES
(1, 'System', 'Administrator', 'admin', '$2a$10$PTXcYdrD4a1qW5MJ8vY67uOkHe.pIpKW0i2xDx.qripSN1bzn1Tfa');

--INSERT INTO attendance_photo (id, photo_data) VALUES
--(1, decode('48656C6C6F2C20776F726C6421', 'hex')),
--(2, decode('48656C6C6F2C20776F726C6421', 'hex'));

INSERT INTO attendance_record (web_user_id, tap_timestamp, tap_date, photo_id) VALUES
(1, '2024-09-06 08:00:00', '2024-09-06', NULL),
(1, '2024-09-06 15:00:00', '2024-09-06', NULL);