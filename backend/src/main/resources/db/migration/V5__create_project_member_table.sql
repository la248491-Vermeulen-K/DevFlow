CREATE TABLE project_member (
                                id BIGINT NOT NULL AUTO_INCREMENT,
                                user_id BIGINT NOT NULL,
                                project_id BIGINT NOT NULL,
                                role VARCHAR(50) NOT NULL,
                                joined_at DATETIME NOT NULL,

                                PRIMARY KEY (id),

                                CONSTRAINT fk_project_member_user
                                    FOREIGN KEY (user_id)
                                        REFERENCES users(id),

                                CONSTRAINT fk_project_member_project
                                    FOREIGN KEY (project_id)
                                        REFERENCES project(id),

                                CONSTRAINT uk_project_member_user_project
                                    UNIQUE (user_id, project_id)
);