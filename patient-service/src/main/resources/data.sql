DROP TABLE IF EXISTS patient;

CREATE TABLE IF NOT EXISTS patient (
                         id UUID PRIMARY KEY,
                         first_name VARCHAR(255) NOT NULL,
                         last_name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         date_of_birth DATE NOT NULL,
                         date_of_registration TIMESTAMP NOT NULL
);

INSERT INTO patient (id, first_name, last_name, email, date_of_birth, date_of_registration) VALUES
                                                                                                ('123e4567-e89b-12d3-a456-426614174000', 'John', 'Doe', 'john.doe1@example.com', '1990-05-15', NOW()),
                                                                                                ('123e4567-e89b-12d3-a456-426614174001', 'Jane', 'Smith', 'jane.smith1@example.com', '1985-08-22', NOW()),
                                                                                                ('123e4567-e89b-12d3-a456-426614174002', 'Alice', 'Brown', 'alice.brown1@example.com', '2000-01-10', NOW()),
                                                                                                ('123e4567-e89b-12d3-a456-426614174003', 'Michael', 'Johnson', 'michael.johnson1@example.com', '1992-07-04', NOW()),
                                                                                                ('123e4567-e89b-12d3-a456-426614174004', 'Emily', 'Davis', 'emily.davis1@example.com', '1988-03-21', NOW()),
                                                                                                ('223e4567-e89b-12d3-a456-426614174005', 'Chris', 'Wilson', 'chris.wilson1@example.com', '1995-11-30', NOW()),
                                                                                                ('223e4567-e89b-12d3-a456-426614174006', 'Jessica', 'Martinez', 'jessica.martinez1@example.com', '1993-06-25', NOW()),
                                                                                                ('223e4567-e89b-12d3-a456-426614174007', 'Daniel', 'Taylor', 'daniel.taylor1@example.com', '1987-09-14', NOW()),
                                                                                                ('223e4567-e89b-12d3-a456-426614174008', 'Sophia', 'Harris', 'sophia.harris1@example.com', '1991-04-12', NOW()),
                                                                                                ('223e4567-e89b-12d3-a456-426614174009', 'David', 'Clark', 'david.clark1@example.com', '1994-12-03', NOW()),

                                                                                                ('223e4567-e89b-12d3-a456-426614174010', 'Olivia', 'Lewis', 'olivia.lewis1@example.com', '1997-01-20', NOW()),
                                                                                                ('223e4567-e89b-12d3-a456-426614174011', 'Ethan', 'Walker', 'ethan.walker1@example.com', '1989-05-27', NOW()),
                                                                                                ('223e4567-e89b-12d3-a456-426614174012', 'Mia', 'Young', 'mia.young1@example.com', '1996-07-18', NOW()),
                                                                                                ('223e4567-e89b-12d3-a456-426614174013', 'James', 'King', 'james.king1@example.com', '1986-09-09', NOW()),
                                                                                                ('223e4567-e89b-12d3-a456-426614174014', 'Ella', 'Scott', 'ella.scott1@example.com', '2001-02-14', NOW()),
                                                                                                ('323e4567-e89b-12d3-a456-426614174015', 'Lucas', 'Adams', 'lucas.adams1@example.com', '1993-11-05', NOW()),
                                                                                                ('323e4567-e89b-12d3-a456-426614174016', 'Ava', 'Baker', 'ava.baker1@example.com', '1998-06-12', NOW()),
                                                                                                ('323e4567-e89b-12d3-a456-426614174017', 'Benjamin', 'Gonzalez', 'benjamin.gonzalez1@example.com', '1992-04-30', NOW()),
                                                                                                ('323e4567-e89b-12d3-a456-426614174018', 'Harper', 'Carter', 'harper.carter1@example.com', '1995-08-01', NOW()),
                                                                                                ('323e4567-e89b-12d3-a456-426614174019', 'Henry', 'Mitchell', 'henry.mitchell1@example.com', '1984-10-17', NOW());
