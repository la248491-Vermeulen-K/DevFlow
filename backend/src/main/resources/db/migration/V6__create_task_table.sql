CREATE TABLE task (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      description TEXT,
                      status VARCHAR(50),
                      project_id BIGINT,
                      created_at DATETIME,

                      CONSTRAINT fk_task_project
                          FOREIGN KEY (project_id)
                              REFERENCES project(id)
                              ON DELETE CASCADE
);