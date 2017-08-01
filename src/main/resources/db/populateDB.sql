DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;
DELETE FROM meals;
ALTER SEQUENCE global_seq2 RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password');

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (datetime, description, calories)
VALUES ('2015-06-01 14:00', 'Админ ланч', 510);

INSERT INTO meals (datetime, description, calories)
VALUES ('2015-06-01 21:00', 'Админ ланч', 1500);
