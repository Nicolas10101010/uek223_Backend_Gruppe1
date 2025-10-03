-- ======================================================================
-- BENUTZER
-- Passwort-Hash = "1234"
-- ======================================================================
INSERT INTO users (id, email, first_name, last_name, password)
VALUES
    ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'admin@example.com', 'James', 'Bond',   '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'),
    ('0d8fa44c-54fd-4cd0-ace9-2a7da57992de', 'user@example.com', 'Tyler', 'Durden', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'),
    ('11111111-1111-1111-1111-111111111111', 'alice@example.com', 'Alice', 'Miller', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'),
    ('22222222-2222-2222-2222-222222222222', 'bob@example.com', 'Bob', 'Johnson', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'),
    ('33333333-3333-3333-3333-333333333333', 'clara@example.com', 'Clara', 'Schmidt', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'),
    ('44444444-4444-4444-4444-444444444444', 'david@example.com', 'David', 'Nguyen', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'),
    ('55555555-5555-5555-5555-555555555555', 'emma@example.com', 'Emma', 'Garcia', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'),
    ('66666666-6666-6666-6666-666666666666', 'felix@example.com', 'Felix', 'Rossi', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'),
    ('77777777-7777-7777-7777-777777777777', 'hannah@example.com', 'Hannah', 'Keller', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'),
    ('88888888-8888-8888-8888-888888888888', 'isaac@example.com', 'Isaac', 'Brown', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'),
    ('99999999-9999-9999-9999-999999999999', 'peter@example.com', 'Peter', 'Keller', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6')

ON CONFLICT DO NOTHING;

-- ======================================================================
-- ROLEN
-- ======================================================================
INSERT INTO role (id, name)
VALUES
    ('ab505c92-7280-49fd-a7de-258e618df074', 'ADMIN'),
    ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', 'USER')
    ON CONFLICT DO NOTHING;

-- Rollen den Benutzern zuweisen
insert into users_role (users_id, role_id)
values ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'ab505c92-7280-49fd-a7de-258e618df074'),
       ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02'),
       ('0d8fa44c-54fd-4cd0-ace9-2a7da57992de', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02'),
       ('11111111-1111-1111-1111-111111111111', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02'),
       ('22222222-2222-2222-2222-222222222222', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02'),
       ('33333333-3333-3333-3333-333333333333', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02'),
       ('44444444-4444-4444-4444-444444444444', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02'),
       ('55555555-5555-5555-5555-555555555555', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02'),
       ('66666666-6666-6666-6666-666666666666', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02'),
       ('77777777-7777-7777-7777-777777777777', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02'),
       ('88888888-8888-8888-8888-888888888888', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02'),
       ('99999999-9999-9999-9999-999999999999', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02')
    ON CONFLICT DO NOTHING;

-- ======================================================================
-- PROFILE FÜR BENUTZER
-- ======================================================================
INSERT INTO user_profile (id, id_user, address, birthdate, profile_img_url, age)
VALUES
    ('81111111-1111-1111-1111-111111111111','0d8fa44c-54fd-4cd0-ace9-2a7da57992de','Paper Street 1, Wilmington','1990-05-15','https://preview.redd.it/blessed-peace-sign-v0-5bqs753g77qd1.jpeg?auto=webp&s=1824130a8b318885b8026df1d85151d973be8295',35),
    ('82222222-2222-2222-2222-222222222222','11111111-1111-1111-1111-111111111111','Main Street 42, Berlin','1995-07-21','https://i.redd.it/prq0ioz813y71.jpg',30),
    ('83333333-3333-3333-3333-333333333333','22222222-2222-2222-2222-222222222222','2nd Avenue 99, New York','1988-03-10','https://static.wikia.nocookie.net/85c88744-a64e-48d5-9b67-66104f8e3525/scale-to-width/755',37),
    ('84444444-4444-4444-4444-444444444444','33333333-3333-3333-3333-333333333333','Hauptstrasse 17, Zürich','1999-11-03','https://i.pinimg.com/564x/eb/a6/c8/eba6c878341c8b978d24457c2cf44e08.jpg',25),
    ('85555555-5555-5555-5555-555555555555','44444444-4444-4444-4444-444444444444','Park Lane 5, London','1992-01-30','https://preview.redd.it/0ixs38andbjc1.jpeg?auto=webp&s=f4eab8e40882173cd9e919440246fadb90680fde',33),
    ('86666666-6666-6666-6666-666666666666','55555555-5555-5555-5555-555555555555','Gran Via 22, Madrid','1997-09-12','https://i.pinimg.com/originals/df/25/96/df2596b086219446585c8c78e6a94a75.jpg',28),
    ('87777777-7777-7777-7777-777777777777','66666666-6666-6666-6666-666666666666','Via Roma 88, Milano','1994-12-05','https://i.pinimg.com/736x/50/59/75/505975a0281b2d860947663d7371cbf8.jpg',30),
    ('88888888-8888-8888-8888-888888888888','77777777-7777-7777-7777-777777777777','Bahnhofstrasse 10, Wien','2000-06-18','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNmQAtdDAxlvvgpf9JzVvVyhoyxpeKW1EhCA&s',24),
    ('89999999-9999-9999-9999-999999999999','88888888-8888-8888-8888-888888888888','Broadway 55, Los Angeles','1985-04-09','https://miro.medium.com/1*3ArAncaR62MGyh-95ufbQw@2x.jpeg',40),
    ('99999999-9999-9999-9999-999999999999','99999999-9999-9999-9999-999999999999','Street 67, Mexiko','1980-06-10','https://i.kym-cdn.com/photos/images/original/002/092/931/f35.jpg',45)
ON CONFLICT DO NOTHING;

-- ======================================================================
-- BERECHTIGUNGEN (AUTHORITIES)
-- ======================================================================

-- BERECHTIGUNGEN (ALLE + EIGENE)
INSERT INTO authority (id, name) VALUES
    ('a1111111-1111-1111-1111-111111111111','USER_READ_ALL'),
    ('a2222222-2222-2222-2222-222222222222','USER_CREATE_ALL'),
    ('a3333333-3333-3333-3333-333333333333','USER_MODIFY_ALL'),
    ('a4444444-4444-4444-4444-444444444444','USER_DELETE_ALL'),
    ('b1111111-1111-1111-1111-111111111111','USER_READ_OWN'),
    ('b3333333-3333-3333-3333-333333333333','USER_MODIFY_OWN'),
    ('b4444444-4444-4444-4444-444444444444','USER_DELETE_OWN')
    ON CONFLICT DO NOTHING;

-- ROLLE BERECHTIGUNG
-- ADMIN: alle Berechtigungen
INSERT INTO role_authority(role_id, authority_id) VALUES
    ('ab505c92-7280-49fd-a7de-258e618df074','a1111111-1111-1111-1111-111111111111'),
    ('ab505c92-7280-49fd-a7de-258e618df074','a2222222-2222-2222-2222-222222222222'),
    ('ab505c92-7280-49fd-a7de-258e618df074','a3333333-3333-3333-3333-333333333333'),
    ('ab505c92-7280-49fd-a7de-258e618df074','a4444444-4444-4444-4444-444444444444')
    ON CONFLICT DO NOTHING;

-- USER: nur Self-Service (nur eigene Daten)
INSERT INTO role_authority(role_id, authority_id) VALUES
    ('c6aee32d-8c35-4481-8b3e-a876a39b0c02','b1111111-1111-1111-1111-111111111111'),
    ('c6aee32d-8c35-4481-8b3e-a876a39b0c02','b3333333-3333-3333-3333-333333333333'),
    ('c6aee32d-8c35-4481-8b3e-a876a39b0c02','b4444444-4444-4444-4444-444444444444')
    ON CONFLICT DO NOTHING;
