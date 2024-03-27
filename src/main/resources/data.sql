MERGE INTO TODO (id, title, description, is_completed, created_at, updated_at)
KEY(id)
VALUES
(1, '買い物リスト', '牛乳, パン, りんご', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, '郵便局に行く', '郵便局で小包を受け取る', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, '家賃の支払い', '家賃を振り込む', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'ジムに行く', '週に2回筋トレ&カーディオ', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, '映画を観る', '新作の映画を観に行く', FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
WHEN NOT MATCHED THEN INSERT;