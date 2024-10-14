CREATE TABLE roles (
                       id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                       name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE refresh_token (
                              id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                              token VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE users (
                       id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                       email VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       token VARCHAR(255),
                       refresh_token_id BIGINT,
                       CONSTRAINT fk_user_refresh_token FOREIGN KEY (refresh_token_id) REFERENCES refresh_token(id) ON DELETE CASCADE
);


CREATE TABLE user_roles (
                            user_id BIGINT,
                            role_id BIGINT,
                            PRIMARY KEY (user_id, role_id),
                            CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
                            CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE tasks (
                       id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                       title VARCHAR(80) NOT NULL,
                       description VARCHAR(300),
                       time_stamp BIGINT NOT NULL,
                       expired_time BIGINT NOT NULL,
                       is_complete BOOLEAN NOT NULL,
                       task_priority VARCHAR(10) NOT NULL,
                       user_id BIGINT,
                       CONSTRAINT fk_task_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE user_tasks (
                            user_id BIGINT,
                            task_id BIGINT,
                            PRIMARY KEY (user_id, task_id),
                            CONSTRAINT fk_user_tasks_user FOREIGN KEY (user_id) REFERENCES users(id),
                            CONSTRAINT fk_user_tasks_task FOREIGN KEY (task_id) REFERENCES tasks(id)
);

CREATE INDEX idx_task_expired_time ON tasks(expired_time);