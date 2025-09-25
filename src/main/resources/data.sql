-- ======================================================================
-- USERS
-- Password-Hash = "1234"
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
    ('88888888-8888-8888-8888-888888888888', 'isaac@example.com', 'Isaac', 'Brown', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6')
    ON CONFLICT DO NOTHING;

-- ======================================================================
-- ROLES
-- ======================================================================
INSERT INTO role (id, name)
VALUES
    ('ab505c92-7280-49fd-a7de-258e618df074', 'ADMIN'),
    ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', 'USER')
    ON CONFLICT DO NOTHING;

-- assign roles to users
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
       ('88888888-8888-8888-8888-888888888888', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02')
ON CONFLICT DO NOTHING;

-- ======================================================================
-- PROFILES FOR USERS
-- ======================================================================
INSERT INTO user_profile (id, id_user, address, birthdate, profile_img_url, age)
VALUES
    ('91111111-1111-1111-1111-111111111111','0d8fa44c-54fd-4cd0-ace9-2a7da57992de','Paper Street 1, Wilmington','1990-05-15','https://picsum.photos/200?u=tyler',35),
    ('92222222-2222-2222-2222-222222222222','11111111-1111-1111-1111-111111111111','Main Street 42, Berlin','1995-07-21','https://picsum.photos/200?u=alice',30),
    ('93333333-3333-3333-3333-333333333333','22222222-2222-2222-2222-222222222222','2nd Avenue 99, New York','1988-03-10','https://picsum.photos/200?u=bob',37),
    ('94444444-4444-4444-4444-444444444444','33333333-3333-3333-3333-333333333333','Hauptstrasse 17, Zürich','1999-11-03','https://picsum.photos/200?u=clara',25),
    ('95555555-5555-5555-5555-555555555555','44444444-4444-4444-4444-444444444444','Park Lane 5, London','1992-01-30','https://picsum.photos/200?u=david',33),
    ('96666666-6666-6666-6666-666666666666','55555555-5555-5555-5555-555555555555','Gran Via 22, Madrid','1997-09-12','https://picsum.photos/200?u=emma',28),
    ('97777777-7777-7777-7777-777777777777','66666666-6666-6666-6666-666666666666','Via Roma 88, Milano','1994-12-05','https://picsum.photos/200?u=felix',30),
    ('98888888-8888-8888-8888-888888888888','77777777-7777-7777-7777-777777777777','Bahnhofstrasse 10, Wien','2000-06-18','https://picsum.photos/200?u=hannah',24),
    ('99999999-9999-9999-9999-999999999999','88888888-8888-8888-8888-888888888888','Broadway 55, Los Angeles','1985-04-09','https://picsum.photos/200?u=isaac',40)
ON CONFLICT DO NOTHING;

-- ======================================================================
-- AUTHORITIES
-- ======================================================================

-- AUTHORITIES (ALL + OWN)
INSERT INTO authority (id, name) VALUES
    ('a1111111-1111-1111-1111-111111111111','USER_READ_ALL'),
    ('a2222222-2222-2222-2222-222222222222','USER_CREATE_ALL'),
    ('a3333333-3333-3333-3333-333333333333','USER_MODIFY_ALL'),
    ('a4444444-4444-4444-4444-444444444444','USER_DELETE_ALL'),
    ('b1111111-1111-1111-1111-111111111111','USER_READ_OWN'),
    ('b3333333-3333-3333-3333-333333333333','USER_MODIFY_OWN'),
    ('b4444444-4444-4444-4444-444444444444','USER_DELETE_OWN')
    ON CONFLICT DO NOTHING;

-- ROLE -> AUTHORITY
-- ADMIN: everything
INSERT INTO role_authority(role_id, authority_id) VALUES
    ('ab505c92-7280-49fd-a7de-258e618df074','a1111111-1111-1111-1111-111111111111'),
    ('ab505c92-7280-49fd-a7de-258e618df074','a2222222-2222-2222-2222-222222222222'),
    ('ab505c92-7280-49fd-a7de-258e618df074','a3333333-3333-3333-3333-333333333333'),
    ('ab505c92-7280-49fd-a7de-258e618df074','a4444444-4444-4444-4444-444444444444')
    ON CONFLICT DO NOTHING;

-- USER: only Self-Service
INSERT INTO role_authority(role_id, authority_id) VALUES
    ('c6aee32d-8c35-4481-8b3e-a876a39b0c02','b1111111-1111-1111-1111-111111111111'),
    ('c6aee32d-8c35-4481-8b3e-a876a39b0c02','b3333333-3333-3333-3333-333333333333'),
    ('c6aee32d-8c35-4481-8b3e-a876a39b0c02','b4444444-4444-4444-4444-444444444444')
    ON CONFLICT DO NOTHING;
