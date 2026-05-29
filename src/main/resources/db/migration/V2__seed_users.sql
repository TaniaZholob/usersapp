INSERT INTO roles(name)
VALUES
    ('ADMIN'),
    ('USER');

-- admin/admin
INSERT INTO users(name, email, password, role_id)
VALUES (
    'Admin',
    'admin@example.com',
    '$2a$10$pfjY/Eg13zNiDyc4bvhQUeV5qAo1ak8hlwiEasIbh9ij9EALbIt2W',
    (SELECT id FROM roles WHERE name = 'ADMIN')
);

-- user/user
INSERT INTO users(name, email, password, role_id)
VALUES (
    'Regular User',
    'user@example.com',
    '$2a$10$wKnuDOQiw4J/HC/oP5cmS.haRVCGvGYricaKbmyApnYJAR.NRT3qG',
    (SELECT id FROM roles WHERE name = 'USER')
);

-- Generate 500 test users
INSERT INTO users(name, email, password, role_id)
SELECT
    'Test User ' || i,
    'user' || i || '@example.com',
    '$2a$10$wKnuDOQiw4J/HC/oP5cmS.haRVCGvGYricaKbmyApnYJAR.NRT3qG',
    (SELECT id FROM roles WHERE name = 'USER')
FROM generate_series(1, 500) AS s(i);