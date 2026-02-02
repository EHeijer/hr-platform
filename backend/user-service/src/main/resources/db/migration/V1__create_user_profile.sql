CREATE TABLE user_profile (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255),
    email VARCHAR(255),
    active BOOLEAN NOT NULL,
    department_id BIGINT
);