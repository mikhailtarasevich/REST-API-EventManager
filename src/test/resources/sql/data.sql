INSERT INTO roles (name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_MANAGER'),
       ('ROLE_PARTICIPANT');

INSERT INTO privileges (name)
VALUES ('PRIVILEGE_APP_ADMIN'),
       ('PRIVILEGE_EVENT_CREATOR'),
       ('PRIVILEGE_PARTICIPANT');

INSERT INTO role_privileges (role_id, privilege_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);

INSERT INTO users (email, password, role_id)
VALUES ('admin@example.com', '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', 1),
       ('manager1@example.com', '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', 2),
       ('manager2@example.com', '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', 2),
       ('manager3@example.com', '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', 2),
       ('participant1@example.com', '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', 3),
       ('participant2@example.com', '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', 3),
       ('participant3@example.com', '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', 3),
       ('participant4@example.com', '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', 3),
       ('participant5@example.com', '$2a$10$9LBpAplZfA8SLaD1iLKtcedvzpPWdRD/aIKzCqlWZcZhL8hZ8KgQK', 3);

INSERT INTO contracts (user_id, status)
VALUES (2, 'PENDING'),
       (3, 'REJECTED'),
       (4, 'ACCEPTED');

INSERT INTO events (name, user_id, description, price)
VALUES ('Event 1', 2, 'Description for Event 1', 100),
       ('Event 2', 3, 'Description for Event 2', 200),
       ('Event 3', 4, 'Description for Event 3', 150),
       ('Event 4', 2, 'Description for Event 5', 500),
       ('Event 5', 2, 'Description for Event 6', 950);

INSERT INTO user_event_participations (user_id, event_id, status, fio, age, covid_passport_number)
VALUES (5, 2, 'PENDING', 'Participant 2', 30, 987654321),
       (5, 3, 'REJECTED', 'Participant 2', 30, 987654321),
       (5, 4, 'ACCEPTED', 'Participant 2', 30, 987654321),
       (6, 3, 'PENDING', 'Participant 3', 28, 456789123),
       (7, 1, 'PENDING', 'Participant 4', 32, 789123456),
       (8, 2, 'PENDING', 'Participant 5', 27, 654321987),
       (9, 2, 'PENDING', 'Participant 5', 27, 654321987);
