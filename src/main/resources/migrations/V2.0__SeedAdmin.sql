INSERT INTO users (date_registered, email, password, username)
VALUES (now(), 'ceci2205@abv.bg', '$2a$10$5b0eljwcY1c.TBjCdBZsRusSRzWUrxyFtVpBtaWnkCMJDWblK1iZi',
        'admin');

-- password for admin: windows

SET @userId := (SELECT id
                FROM users
                WHERE username = 'admin');

INSERT INTO roles (id, role)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN'),
       (3, 'ROLE_GOD');

INSERT INTO users_roles(user_id, role_id)
VALUES (@userId, 1),
       (@userId, 2),
       (@userId, 3);
