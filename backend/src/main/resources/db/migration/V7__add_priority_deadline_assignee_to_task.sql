ALTER TABLE task
    ADD COLUMN priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    ADD COLUMN deadline DATETIME NULL,
    ADD COLUMN assignee_id BIGINT NULL,
    ADD CONSTRAINT fk_task_assignee
        FOREIGN KEY (assignee_id)
        REFERENCES users(id);