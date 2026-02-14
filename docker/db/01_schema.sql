

-- 権限用のENUM型
CREATE TYPE role AS ENUM ('ADMIN', 'USER');

-- 質問に対する回答タイプのENUM型
CREATE TYPE question_type AS ENUM (
	'RADIO', 'CHECKBOX', 'TEXT');

-- 集計結果の公開範囲ENUM型
CREATE TYPE result_visibility AS ENUM ('ADMIN_ONLY', 'TARGET_GROUP', 'ALL_USER');

-- アンケートステータスENUM型
CREATE TYPE survey_status AS ENUM ('DRAFT', 'PUBLISHED', 'CLOSED', 'DELETED');

-- 回答ステータスENUM型
CREATE TYPE response_status AS ENUM ('UNVERIFIED', 'VALID', 'DUPLICATE', 'INVALID', 'TEST');


-- アンケート本体のテーブル作成
CREATE TABLE surveys (
	-- id : 内部管理用ID (主キー)
	id serial PRIMARY KEY,
	-- title (アンケートタイトル)
	title VARCHAR(255) NOT NULL,
	-- status (ステータス)
	status survey_status NOT NULL DEFAULT 'DRAFT',
	-- result_visibility (集計結果の公開範囲)
	result_visibility result_visibility NOT NULL DEFAULT 'ADMIN_ONLY',
	-- deadline (回答締め切り日時) NULLの場合は無期限
	deadline timestamp without time zone,
	-- created_at (作成日)
	created_at timestamp without time zone,
	-- updated_at (更新日)
	updated_at timestamp without time zone
);

-- 認証情報を格納するテーブル
CREATE TABLE authentications (
	--ユーザー名 : 主キー
	username VARCHAR(50) PRIMARY KEY,
	--- パスワード
	password VARCHAR(255) NOT NULL,
	--- メールアドレス (RFC準拠で255文字あれば十分)
	email VARCHAR(255) NOT NULL UNIQUE,
	--- 表示名
	display_name VARCHAR(50) NOT NULL,
	-- created_at (作成日)
	created_at timestamp without time zone,
	-- updated_at (更新日)
	updated_at timestamp without time zone,
	--- 有効フラグ (論理削除用: TRUE=有効, FALSE=無効/削除済み)
	enabled BOOLEAN NOT NULL DEFAULT TRUE

);


-- ユーザーグループテーブル
CREATE TABLE user_groups (
	id serial PRIMARY KEY,
	group_name VARCHAR(100) NOT NULL,
	-- グループに紐づく権限
	authority role NOT NULL DEFAULT 'USER',
	-- システム上必須のグループかどうか(削除不可フラグ)
	is_system_group BOOLEAN NOT NULL DEFAULT FALSE
);

-- 回答パターン(親)テーブル
-- TODO: 登録前に「同じ選択肢のパターン」がないか検索すると、ユーザーフレンドリーかもしれない
CREATE TABLE answer_patterns (
	id serial PRIMARY KEY,
    -- 同じ名称は回答パターン選択項目が分かりずらくなる、重複を避けるなどの理由で許可しない
	pattern_name VARCHAR(100) NOT NULL, -- 管理用名称(例：5段階評価、Yes/No)
	is_deleted BOOLEAN NOT NULL DEFAULT FALSE, -- 論理削除フラグ
	is_snapshot BOOLEAN NOT NULL DEFAULT FALSE -- スナップショットフラグ
);
-- テンプレート(スナップショット以外)のみ名称の重複を禁止する
CREATE UNIQUE INDEX answer_patterns_name_idx ON answer_patterns (pattern_name) WHERE is_snapshot = FALSE;

-- 回答パターンの選択肢(子)テーブル
CREATE TABLE answer_pattern_items (
	id serial PRIMARY KEY,
	answer_pattern_id INTEGER NOT NULL REFERENCES answer_patterns(id) ON DELETE CASCADE,
	item_text VARCHAR(255) NOT NULL,
    -- 表示順序
	item_order INTEGER NOT NULL,
	-- スナップショットフラグ
	is_snapshot BOOLEAN NOT NULL DEFAULT FALSE
);

-- 設問用のテーブル作成
CREATE TABLE questions (
	-- id : 内部管理用ID (主キー)
	id serial PRIMARY KEY,
	-- survey_id : アンケートID (外部キー)
	survey_id INTEGER NOT NULL REFERENCES surveys(id) ON DELETE CASCADE,
	-- answer_pattern_id : 回答パターンID (外部キー) ※自由記述の場合はNULL
	answer_pattern_id INTEGER REFERENCES answer_patterns(id) ON DELETE SET NULL,
	-- question_text (質問文)
	question_text text,
	-- mode (回答形式)
	question_type question_type NOT NULL,
	-- is_required (必須回答かどうか)
	is_required BOOLEAN NOT NULL DEFAULT FALSE,
	-- display_order (表示順)
	display_order INTEGER NOT NULL DEFAULT 0,
	-- is_deleted (論理削除フラグ)
	is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- 回答ヘッダーテーブル(誰がいつ回答したか)
CREATE TABLE responses (
	id serial PRIMARY KEY,
	survey_id INTEGER NOT NULL REFERENCES surveys(id) ON DELETE CASCADE,
	-- ログインユーザー (任意: 外部キー制約を活用するため独立させる)
	username VARCHAR(50) REFERENCES authentications(username) ON DELETE CASCADE,
	-- 匿名回答時の識別子 (例: "ip:192.168.1.1", "session:xyz...", "cookie:abc...")
	trace_id VARCHAR(255),
	-- status : 回答の状態 (有効、無効、テストなど)
	status response_status NOT NULL DEFAULT 'UNVERIFIED',
	-- created_at (作成日)
	created_at timestamp without time zone,
	-- updated_at (更新日: 再回答された日時)
	updated_at timestamp without time zone
);

-- 回答明細テーブル(どの設問にどう答えたか)
CREATE TABLE response_details (
	id serial PRIMARY KEY,
	response_id INTEGER NOT NULL REFERENCES responses(id) ON DELETE CASCADE,
	question_id INTEGER NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
	answer_pattern_item_id INTEGER REFERENCES answer_pattern_items(id) ON DELETE SET NULL, -- 選択肢を選んだ場合
	answer_text text -- 自由記述の場合
);


-- アンケートと対象グループの紐づけ
CREATE TABLE survey_target_groups (
	survey_id INTEGER NOT NULL REFERENCES surveys(id) ON DELETE CASCADE,
	group_id INTEGER NOT NULL REFERENCES user_groups(id) ON DELETE CASCADE,
	PRIMARY KEY (survey_id, group_id)
);

-- ユーザーとグループの紐付け(多対多)
CREATE TABLE user_group_mappings (
	username VARCHAR(50) NOT NULL REFERENCES authentications(username) ON DELETE CASCADE,
	group_id INTEGER NOT NULL REFERENCES user_groups(id) ON DELETE CASCADE,
	PRIMARY KEY (username, group_id)
);
