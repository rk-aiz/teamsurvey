-- 既存のデータをクリア(外部キー制約により関連テーブルも削除される想定)
DELETE FROM surveys;

-- テスト用データの投入
INSERT INTO surveys (title, status, result_visibility, created_at, updated_at) VALUES ('テストアンケートA', 'PUBLISHED', 'ALL_USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO surveys (title, status, result_visibility, created_at, updated_at) VALUES ('テストアンケートB', 'DRAFT', 'ADMIN_ONLY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);