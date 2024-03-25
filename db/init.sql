CREATE USER docker;
CREATE DATABASE bookshelf;
GRANT ALL PRIVILEGES ON DATABASE bookshelf TO docker;
\c bookshelf
CREATE TABLE IF NOT EXISTS book (
  id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  author_id INT,
  publication_date DATE,
  is_deleted BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON COLUMN book.id IS '書籍ID';
COMMENT ON COLUMN book.title IS 'タイトル';
COMMENT ON COLUMN book.author_id IS '著者ID';
COMMENT ON COLUMN book.publication_date IS '出版日';
COMMENT ON COLUMN book.is_deleted IS '削除フラグ';
COMMENT ON COLUMN book.created_at IS '作成日';
COMMENT ON COLUMN book.updated_at IS '更新日';

INSERT INTO book (id, title, author_id, publication_date, is_deleted, created_at, updated_at)
VALUES
    (1, 'ノルウェイの森', 1, '1987-9-4', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, '吾輩は猫である', 2, '1905-1-1', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, '羅生門', 3, '1971-3-5', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 'Harry Potter and the Philosopher’s Stone', 4, '1997-6-26', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, '罪と罰', 5, '1866-12-1', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id)
DO UPDATE SET
    title = EXCLUDED.title,
    author_id = EXCLUDED.author_id,
    publication_date = EXCLUDED.publication_date,
    is_deleted = EXCLUDED.is_deleted,
    created_at = EXCLUDED.created_at,
    updated_at = EXCLUDED.updated_at;
