-- 回答パターンの登録
-- パターン1: 難易度 (ID=1)
INSERT INTO answer_patterns (id, pattern_name) VALUES (1, '難易度(5段階)');
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (1, 'とても難しい', 1);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (1, '難しい', 2);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (1, '普通', 3);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (1, '簡単', 4);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (1, 'とても簡単', 5);

-- パターン2: Yes/No (ID=2)
INSERT INTO answer_patterns (id, pattern_name) VALUES (2, 'Yes/No');
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (2, 'はい', 1);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (2, 'いいえ', 2);

-- パターン3: 満足度 (ID=3)
INSERT INTO answer_patterns (id, pattern_name) VALUES (3, '満足度(5段階)');
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (3, '5 (最高)', 1);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (3, '4', 2);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (3, '3', 3);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (3, '2', 4);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (3, '1 (最低)', 5);

-- 1件目のデータ登録
-- 1件目: 複数の質問を持つアンケート (ID=1)
INSERT INTO surveys (title, status, result_visibility, created_at, updated_at)
VALUES ('ITエンジニア意識調査', 'PUBLISHED', 'TARGET_GROUP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Q1 (ID=1)
-- パターンID=1 (難易度) を使用
INSERT INTO questions (id, survey_id, question_text, question_type, is_required, answer_pattern_id, display_order)
VALUES (1, 1, 'Javaは難しいですか?', 'RADIO', TRUE, 1, 1);

-- Q2 (ID=2)
-- パターンID=2 (Yes/No) を使用
INSERT INTO questions (id, survey_id, question_text, question_type, is_required, answer_pattern_id, display_order)
VALUES (2, 1, 'Spring Frameworkは好きですか?', 'RADIO', FALSE, 2, 2);

-- 2件目: 全種類の回答形式を含むアンケート (ID=2)
INSERT INTO surveys (title, status, result_visibility, created_at, updated_at)
VALUES ('サービス満足度調査', 'PUBLISHED', 'ALL_USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Q3 (ID=3)
-- パターンID=2 (Yes/No) を再利用（文脈に合わせて解釈）
INSERT INTO questions (id, survey_id, question_text, question_type, is_required, answer_pattern_id, display_order)
VALUES (3, 2, '今回のサービスを利用して満足しましたか？', 'RADIO', TRUE, 2, 1);

-- Q4 (ID=4)
-- パターンID=3 (満足度) を使用
INSERT INTO questions (id, survey_id, question_text, question_type, is_required, answer_pattern_id, display_order)
VALUES (4, 2, 'サービスの品質を5段階で評価してください', 'RADIO', TRUE, 3, 2);

-- Q5 (ID=5)
-- 自由記述なのでパターンIDはNULL
INSERT INTO questions (id, survey_id, question_text, question_type, is_required, answer_pattern_id, display_order)
VALUES (5, 2, 'その他、ご意見・ご要望があればご記入ください', 'TEXT', FALSE, NULL, 3);

-- 3件目: 質問がまだないアンケート (ID=3) -> LEFT JOINの動作確認用
INSERT INTO surveys (title, status, result_visibility, created_at, updated_at)
VALUES ('【下書き】新規企画アンケート', 'DRAFT', 'ADMIN_ONLY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 4件目: テキスト回答のみのアンケート (ID=4)
INSERT INTO surveys (title, status, result_visibility, created_at, updated_at)
VALUES ('社内イベント感想', 'CLOSED', 'ALL_USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Q6 (ID=6)
INSERT INTO questions (id, survey_id, question_text, question_type, is_required, answer_pattern_id, display_order)
VALUES (6, 4, 'イベントで最も印象に残ったことは何ですか？', 'TEXT', TRUE, NULL, 1);

-- 認証テーブルにダミーの認証データを登録
INSERT INTO authentications (username, password, display_name, email)
VALUES ('admin', 
		'$2a$10$aVhzT738nhBa.eTbZZib4ey0N8X2Xek6ulC47lYmhhXpm1HEdOv9W', 
		'菅理太郎',
		'admin@example.com');

-- 認証テーブルにダミーの認証データを登録
INSERT INTO authentications (username, password, display_name, email)
VALUES ('user', 
		'$2a$10$/jar9xXQ6lrnVjLvLGv5BepFkLnGIO49RrGx42p2i.1hQt1BZ/7E2', 
		'一般花子',
		'user@example.com');

-- ユーザーグループの登録
INSERT INTO user_groups (id, group_name, authority, is_system_group) VALUES (1, '全社員', 'USER', TRUE);
INSERT INTO user_groups (id, group_name, authority, is_system_group) VALUES (2, 'エンジニア', 'USER', FALSE);
INSERT INTO user_groups (id, group_name, authority, is_system_group) VALUES (99, 'システム管理者', 'ADMIN', TRUE);

-- ユーザーとグループの紐付け
INSERT INTO user_group_mappings (username, group_id) VALUES ('user', 1);
INSERT INTO user_group_mappings (username, group_id) VALUES ('user', 2);
-- adminユーザーを管理者グループに所属させる
INSERT INTO user_group_mappings (username, group_id) VALUES ('admin', 99);

-- アンケートの公開範囲設定 (ID=1のアンケートはエンジニアのみ)
INSERT INTO survey_target_groups (survey_id, group_id) VALUES (1, 2);

-- 【重要】PostgreSQL特有の処理
-- IDを指定してINSERTした場合、自動採番のシーケンスが進まないため、
-- 次にアプリからINSERTすると「ID重複エラー」になる。
-- 手動でシーケンス値を現在の最大IDに合わせておく。

-- surveysテーブルのIDシーケンスを更新
SELECT setval(pg_get_serial_sequence('surveys', 'id'), (SELECT MAX(id) FROM surveys));

-- questionsテーブルのIDシーケンスを更新
SELECT setval(pg_get_serial_sequence('questions', 'id'), (SELECT MAX(id) FROM questions));

-- answer_patternsテーブルのIDシーケンスを更新
SELECT setval(pg_get_serial_sequence('answer_patterns', 'id'), (SELECT MAX(id) FROM answer_patterns));

-- answer_pattern_itemsテーブルのIDシーケンスを更新
SELECT setval(pg_get_serial_sequence('answer_pattern_items', 'id'), (SELECT MAX(id) FROM answer_pattern_items));

-- ※もしテーブル作成時に GENERATED BY DEFAULT AS IDENTITY を使っている場合は
--   OVERRIDING SYSTEM VALUE 句が必要だが、
--   通常の SERIAL 型であれば上記 setval で大丈夫。