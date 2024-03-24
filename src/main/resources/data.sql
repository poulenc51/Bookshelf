MERGE INTO BOOK USING (VALUES
    (1, 'ノルウェイの森', 1, '1987-9-4', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, '吾輩は猫である', 2, '1905-1-1', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, '羅生門', 3, '1971-3-5', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 'Harry Potter and the Philosopher’s Stone', 4, '1997-6-26', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, '罪と罰', 5, '1866-12-1', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
) AS source (id, title, author_id, publication_date, is_deleted, created_at, updated_at)
ON BOOK.id = source.id
WHEN NOT MATCHED THEN
    INSERT (id, title, author_id, publication_date, is_deleted, created_at, updated_at)
    VALUES (source.id, source.title, source.author_id, source.publication_date, source.is_deleted, source.created_at, source.updated_at);