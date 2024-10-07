CREATE TABLE roles (
                       id BIGINT PRIMARY KEY,
                       name VARCHAR(20) NOT NULL
);

CREATE TABLE users (
                       id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                       email VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       fcm_token VARCHAR(255),
                       refresh_token_id BIGINT,
                       CONSTRAINT fk_refresh_token FOREIGN KEY (refresh_token_id) REFERENCES refresh_token(id)
);

CREATE TABLE user_roles (
                            user_id BIGINT,
                            role_id BIGINT,
                            PRIMARY KEY (user_id, role_id),
                            CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
                            CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE tasks (
                       id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                       title VARCHAR(255),
                       timestamp BIGINT NOT NULL,
                       expired_time BIGINT NOT NULL,
                       is_complete BOOLEAN DEFAULT FALSE,
                       task_priority VARCHAR(10) NOT NULL,
                       user_id BIGINT,
                       CONSTRAINT fk_user_task FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_timestamp ON tasks (timestamp);