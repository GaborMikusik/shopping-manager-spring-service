INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO users (name, username, email, password) VALUES ('name', 'username', 'test@example.com', 'password');
INSERT INTO user_roles (user_id, role_id) VALUES ((SELECT id FROM users WHERE username = 'username'), (SELECT id FROM roles WHERE name = 'ROLE_USER'));