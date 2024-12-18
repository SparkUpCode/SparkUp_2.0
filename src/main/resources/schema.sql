CREATE TABLE IF NOT EXISTS task (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    completed BOOLEAN DEFAULT FALSE,
    status VARCHAR(20),
    assigned_username VARCHAR(255),
    comment TEXT,
    pull_request_url VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS project_tasks (
    project_id BIGINT,
    tasks_id BIGINT,
    CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES project(id),
    CONSTRAINT fk_task FOREIGN KEY (tasks_id) REFERENCES task(id)
); 