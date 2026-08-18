CREATE TABLE task_label (
                            task_id BIGINT NOT NULL,
                            label_id BIGINT NOT NULL,

                            PRIMARY KEY (task_id, label_id),

                            CONSTRAINT fk_task_label_task
                                FOREIGN KEY (task_id)
                                    REFERENCES task(id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_task_label_label
                                FOREIGN KEY (label_id)
                                    REFERENCES label(id)
                                    ON DELETE CASCADE
);