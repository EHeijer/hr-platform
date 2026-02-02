CREATE TABLE user_profile_role (
    user_id VARCHAR(36) NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY(user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user_profile(id),
    FOREIGN KEY (role_id) REFERENCES work_role(id)
);