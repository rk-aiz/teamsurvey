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

-- パターン4: 同意の程度 (ID=4)
INSERT INTO answer_patterns (id, pattern_name) VALUES (4, '同意の程度(5段階)');
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (4, '強く同意する', 1);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (4, '同意する', 2);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (4, 'どちらともいえない', 3);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (4, '同意しない', 4);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (4, '全く同意しない', 5);

-- パターン5: 頻度 (ID=5)
INSERT INTO answer_patterns (id, pattern_name) VALUES (5, '頻度(5段階)');
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (5, 'いつも', 1);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (5, 'よくある', 2);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (5, '時々', 3);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (5, 'あまりない', 4);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (5, '全くない', 5);

-- パターン6: 推奨意向 (ID=6)
INSERT INTO answer_patterns (id, pattern_name) VALUES (6, '推奨意向(5段階)');
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (6, '非常に勧めたい', 1);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (6, '勧めたい', 2);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (6, 'どちらともいえない', 3);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (6, 'あまり勧めたくない', 4);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (6, '全く勧めたくない', 5);

-- パターン7: 利用意向 (ID=7)
INSERT INTO answer_patterns (id, pattern_name) VALUES (7, '利用意向(5段階)');
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (7, 'ぜひ利用したい', 1);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (7, '利用したい', 2);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (7, 'どちらともいえない', 3);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (7, 'あまり利用したくない', 4);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (7, '全く利用したくない', 5);

-- パターン8: 重要度 (ID=8)
INSERT INTO answer_patterns (id, pattern_name) VALUES (8, '重要度(5段階)');
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (8, '非常に重要', 1);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (8, '重要', 2);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (8, 'どちらともいえない', 3);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (8, 'あまり重要ではない', 4);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (8, '全く重要ではない', 5);

-- パターン9: 認知経路 (ID=9)
INSERT INTO answer_patterns (id, pattern_name) VALUES (9, '認知経路');
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (9, 'Web検索(Google/Yahoo!など)', 1);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (9, 'SNS(X/Instagram/Facebookなど)', 2);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (9, '知人・友人の紹介', 3);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (9, 'テレビ・CM', 4);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (9, 'Web広告・バナー', 5);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (9, 'YouTube・動画サイト', 6);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (9, '店頭・ポスター', 7);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (9, 'その他', 8);

-- パターン10: Yes/No/分からない (ID=10)
INSERT INTO answer_patterns (id, pattern_name) VALUES (10, 'Yes/No/分からない');
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (10, 'はい', 1);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (10, 'いいえ', 2);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (10, '分からない', 3);

-- パターン11: 期間 (ID=11)
INSERT INTO answer_patterns (id, pattern_name) VALUES (11, '期間(年数)');
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (11, '1年未満', 1);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (11, '1年以上3年未満', 2);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (11, '3年以上5年未満', 3);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (11, '5年以上10年未満', 4);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (11, '10年以上', 5);

-- パターン12: 選定理由 (ID=12)
INSERT INTO answer_patterns (id, pattern_name) VALUES (12, '選定理由');
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (12, '価格が安いから', 1);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (12, '機能・性能が良いから', 2);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (12, 'デザインが気に入ったから', 3);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (12, '口コミ・評判が良いから', 4);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (12, '以前から知っていたから', 5);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (12, 'キャンペーンや特典があったから', 6);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (12, '他社製品より優れていたから', 7);

-- パターン13: 利用シーン・環境 (ID=13)
INSERT INTO answer_patterns (id, pattern_name) VALUES (13, '利用シーン・環境');
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (13, 'スマートフォン', 1);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (13, 'ノートPC', 2);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (13, 'デスクトップPC', 3);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (13, 'タブレット', 4);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (13, '職場・オフィス', 5);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (13, '自宅', 6);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (13, '移動中・外出先', 7);

-- パターン14: 興味・関心 (ID=14)
INSERT INTO answer_patterns (id, pattern_name) VALUES (14, '興味・関心');
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (14, 'プログラミング・開発', 1);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (14, 'デザイン・クリエイティブ', 2);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (14, 'ビジネス・マーケティング', 3);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (14, '語学・教育', 4);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (14, '健康・フィットネス', 5);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (14, '投資・金融', 6);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (14, '料理・グルメ', 7);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (14, '旅行・アウトドア', 8);

-- パターン15: 困りごと・課題 (ID=15)
INSERT INTO answer_patterns (id, pattern_name) VALUES (15, '困りごと・課題');
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (15, 'コストがかかりすぎている', 1);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (15, '人手が不足している', 2);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (15, '専門知識が不足している', 3);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (15, '時間がかかりすぎている', 4);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (15, '品質が安定しない', 5);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (15, 'ツールの使い方が難しい', 6);
INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (15, '特に課題はない', 7);

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
-- パターンID=2 (Yes/No) を再利用(文脈に合わせて解釈)
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

-- PostgreSQL特有の処理
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

-- responsesテーブルのIDシーケンスを更新
SELECT setval(pg_get_serial_sequence('responses', 'id'), (SELECT MAX(id) FROM responses));

SELECT setval(pg_get_serial_sequence('response_details', 'id'), (SELECT MAX(id) FROM response_details));
-- ※もしテーブル作成時に GENERATED BY DEFAULT AS IDENTITY を使っている場合は
--   OVERRIDING SYSTEM VALUE 句が必要だが、
--   通常の SERIAL 型であれば上記 setval で大丈夫。

SELECT setval(pg_get_serial_sequence('user_groups', 'id'), (SELECT MAX(id) FROM user_groups));