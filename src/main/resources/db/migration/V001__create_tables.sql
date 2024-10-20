CREATE TABLE roles (
                       id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                       name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE refresh_token (
                               id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               token VARCHAR(255) NOT NULL UNIQUE,
                               counter INT NOT NULL,
                               timestamp BIGINT NOT NULL
);

CREATE TABLE users (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       email VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       refresh_token_id BIGINT,
                       CONSTRAINT fk_refresh_token
                           FOREIGN KEY (refresh_token_id)
                               REFERENCES refresh_token(id)
                               ON DELETE CASCADE
);


CREATE TABLE user_roles (
                            user_id BIGINT,
                            role_id BIGINT,
                            CONSTRAINT fk_user
                                FOREIGN KEY (user_id)
                                    REFERENCES users(id)
                                    ON DELETE CASCADE,
                            CONSTRAINT fk_role
                                FOREIGN KEY (role_id)
                                    REFERENCES roles(id)
                                    ON DELETE CASCADE,
                            PRIMARY KEY (user_id, role_id)
);

CREATE TABLE tasks (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       title VARCHAR(80) NOT NULL,
                       description VARCHAR(300),
                       timestamp BIGINT NOT NULL,
                       expired BIGINT NOT NULL,
                       iscomplete BOOLEAN NOT NULL,
                       taskpriority VARCHAR(10) NOT NULL,
                       user_id BIGINT,
                       CONSTRAINT fk_user_task
                           FOREIGN KEY (user_id)
                               REFERENCES users(id)
                               ON DELETE CASCADE
);

CREATE TABLE user_tasks (
                            user_id BIGINT,
                            task_id BIGINT,
                            PRIMARY KEY (user_id, task_id),
                            CONSTRAINT fk_user_tasks_user FOREIGN KEY (user_id) REFERENCES users(id),
                            CONSTRAINT fk_user_tasks_task FOREIGN KEY (task_id) REFERENCES tasks(id)
);

CREATE INDEX idx_task_expired ON tasks(expired);