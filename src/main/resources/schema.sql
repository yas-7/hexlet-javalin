DROP TABLE IF EXISTS courses;

CREATE TABLE courses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

INSERT INTO courses (name, description) VALUES
('Java', 'Basics of java'),
('JavaScript', 'Fundamentals of javascript');

DROP TABLE IF EXISTS cars;

CREATE TABLE cars (
    id SERIAL PRIMARY KEY,
    make VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL
);
