CREATE TABLE label (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       color VARCHAR(255),
                       project_id BIGINT,

                       CONSTRAINT fk_label_project
                           FOREIGN KEY (project_id)
                               REFERENCES project(id)
                               ON DELETE CASCADE
);