CREATE DATABASE flexlearn_database;

USE flexlearn_database;

# What topic interest the user
CREATE TABLE interests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    full_name NVARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_type VARCHAR(50) NOT NULL, # Admin, User
    preferred_language VARCHAR(255),
    job_title VARCHAR(255) NULLABLE, -- Student, Teacher, Employee
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

# middleware for users and interests table because they are Many to Many relations
CREATE TABLE user_interests (
    user_id BIGINT NOT NULL,
    interest_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, interest_id),

    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ,
    CONSTRAINT fk_interest FOREIGN KEY (interest_id) REFERENCES interests(id) ON DELETE CASCADE
);

CREATE TABLE courses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name NVARCHAR (255) CHARACTER SET utf8mb4 NOT NULL,
    image VARCHAR(255) NULLABLE,
    author NVARCHAR (255) CHARACTER SET utf8mb4 NOT NULL,
    type VARCHAR(255) NOT NULL, -- Self-paced/Live Boot Camp
    price DECIMAL(10, 2) NOT NULL create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIME ON UPDATE CURRENT_TIMESTAMP
);

-- one to many
CREATE TABLE course_cohorts (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    course_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    name VARCHAR(50),
    CONSTRAINT fk_cohort_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE
);

CREATE TABLE enrollments (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL
    -- save cohort day that user selected
    selected_cohort_date DATE,

    status VARCHAR(50) -- PENDING, PAID, COMPLETED
);