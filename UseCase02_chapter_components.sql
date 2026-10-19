CREATE TABLE chapter_components (
    component_id   INT AUTO_INCREMENT PRIMARY KEY,
    chapter_id     INT NOT NULL,
    component_type VARCHAR(50) NOT NULL,
    sort_order     INT NOT NULL,
    content        TEXT NULL,
    UNIQUE KEY uq_chapter_component (chapter_id, component_type),
    CONSTRAINT fk_component_chapter FOREIGN KEY (chapter_id) REFERENCES chapters(chapter_id)
        ON DELETE CASCADE
);