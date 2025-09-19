-- ======================================================================
-- USERS
-- Password-Hash = "1234"
-- ======================================================================
INSERT INTO users (id, email, first_name, last_name, password)
VALUES
    ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'admin@example.com', 'James', 'Bond',   '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6'),
    ('0d8fa44c-54fd-4cd0-ace9-2a7da57992de', 'user@example.com',  'Tyler', 'Durden', '$2a$10$TM3PAYG3b.H98cbRrHqWa.BM7YyCqV92e/kUTBfj85AjayxGZU7d6')
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
       ('0d8fa44c-54fd-4cd0-ace9-2a7da57992de', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02')
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
