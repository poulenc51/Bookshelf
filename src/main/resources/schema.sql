CREATE TABLE IF NOT EXISTS BOOK (
  id INT GENERATED BY DEFAULT AS IDENTITY COMMENT '書籍ID',
  title VARCHAR(100) NOT NULL COMMENT 'タイトル',
  author_id INT COMMENT '著者ID',
  publication_date DATE COMMENT '出版日',
  is_deleted BOOLEAN DEFAULT FALSE COMMENT '削除フラグ',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '作成日',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新日'
);