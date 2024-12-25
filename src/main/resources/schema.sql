-- Create enum type for task status


-- Drop tables in correct order (dependent tables first)
DROP TABLE IF EXISTS project_tasks CASCADE;
DROP TABLE IF EXISTS recommendations CASCADE;
DROP TABLE IF EXISTS key_achievements CASCADE;
DROP TABLE IF EXISTS work_experience CASCADE;
DROP TABLE IF EXISTS task CASCADE;
DROP TABLE IF EXISTS project CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create tables in correct order (independent tables first)
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
    title VARCHAR(10000),
    description VARCHAR(10000),
    stage VARCHAR(10000),
    industry VARCHAR(10000),
    link_to_project VARCHAR(10000),
    problem TEXT,
    solution TEXT,
    prototype TEXT,
    ideal_customer TEXT,
    competitors VARCHAR(10000),
    goals VARCHAR(10000),
    CONSTRAINT fk_creator_username FOREIGN KEY (creator_username) REFERENCES users(username)
);

CREATE TABLE IF NOT EXISTS task (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    completed BOOLEAN DEFAULT FALSE,
    status VARCHAR(255) CHECK (status IN ('OPEN', 'ACTIVE', 'PENDING_APPROVAL', 'COMPLETED')),
    category VARCHAR(255) CHECK (category IN ('SYSTEM_DESIGN', 'UX_UI', 'BACKEND', 'FRONTEND', 'DATABASE')),
    estimated_hours DOUBLE PRECISION,
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

CREATE TABLE IF NOT EXISTS work_experience (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    job_title VARCHAR(255),
    company VARCHAR(255),
    date_start DATE,
    date_finish DATE,
    present_employment BOOLEAN DEFAULT FALSE,
    job_summary TEXT,
    visible BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS key_achievements (
    work_experience_id BIGINT,
    achievement TEXT,
    CONSTRAINT fk_work_experience FOREIGN KEY (work_experience_id) REFERENCES work_experience(id)
);

CREATE TABLE IF NOT EXISTS recommendations (
    id BIGSERIAL PRIMARY KEY,
    work_experience_id BIGINT,
    name VARCHAR(255),
    position VARCHAR(255),
    comment TEXT,
    verified BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_work_experience_rec FOREIGN KEY (work_experience_id) REFERENCES work_experience(id)
); 