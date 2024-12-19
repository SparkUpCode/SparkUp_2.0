CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    email VARCHAR(255),
    password VARCHAR(255),
    roles VARCHAR(255),
    bio VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS project (
    id BIGSERIAL PRIMARY KEY,
    creator_username VARCHAR(255),
    problem TEXT,
    solution TEXT,
    prototype TEXT,
    ideal_customer TEXT,
    CONSTRAINT fk_creator_username FOREIGN KEY (creator_username) REFERENCES users(username)
);

CREATE TABLE IF NOT EXISTS task (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    completed BOOLEAN DEFAULT FALSE,
    status VARCHAR(20),
    assigned_username VARCHAR(255),
    comment TEXT,
    pull_request_url VARCHAR(255),
    CONSTRAINT fk_assigned_username FOREIGN KEY (assigned_username) REFERENCES users(username)
);

CREATE TABLE IF NOT EXISTS project_tasks (
    project_id BIGINT,
    tasks_id BIGINT,
    CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES project(id),
    CONSTRAINT fk_task FOREIGN KEY (tasks_id) REFERENCES task(id)
); 