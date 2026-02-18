-- テスト用スキーマ定義 (H2 Database互換)
-- PostgreSQL固有の CREATE TYPE ... ENUM を排除し、VARCHARで代用しています。

DROP TABLE IF EXISTS surveys CASCADE;
DROP TABLE IF EXISTS questions CASCADE;
DROP TABLE IF EXISTS answer_patterns CASCADE;
DROP TABLE IF EXISTS answer_pattern_items CASCADE;
DROP TABLE IF EXISTS responses CASCADE;
DROP TABLE IF EXISTS response_details CASCADE;
DROP TABLE IF EXISTS authentications CASCADE;
DROP TABLE IF EXISTS survey_target_groups CASCADE;
DROP TABLE IF EXISTS user_group_mappings CASCADE;
DROP TABLE IF EXISTS user_groups CASCADE;

-- アンケート本体のテーブル作成
CREATE TABLE surveys (
	id serial PRIMARY KEY,
	title VARCHAR(255) NOT NULL,
	-- ENUM -> VARCHAR
	status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
	result_visibility VARCHAR(50) NOT NULL DEFAULT 'ADMIN_ONLY',
	deadline timestamp without time zone,
	created_at timestamp without time zone,
	updated_at timestamp without time zone
);

-- 認証情報を格納するテーブル
CREATE TABLE authentications (
	username VARCHAR(50) PRIMARY KEY,
	password VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL UNIQUE,
	display_name VARCHAR(50) NOT NULL,
	created_at timestamp without time zone,
	updated_at timestamp without time zone,
	enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- ユーザーグループテーブル
CREATE TABLE user_groups (
	id serial PRIMARY KEY,
	group_name VARCHAR(100) NOT NULL,
	-- ENUM -> VARCHAR
	authority VARCHAR(50) NOT NULL DEFAULT 'USER',
	is_system_group BOOLEAN NOT NULL DEFAULT FALSE
);

-- 回答パターン(親)テーブル
CREATE TABLE answer_patterns (
	id serial PRIMARY KEY,
	pattern_name VARCHAR(100) NOT NULL,
	is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
	is_snapshot BOOLEAN NOT NULL DEFAULT FALSE
);

-- 回答パターンの選択肢(子)テーブル
CREATE TABLE answer_pattern_items (
	id serial PRIMARY KEY,
	answer_pattern_id INTEGER NOT NULL REFERENCES answer_patterns(id) ON DELETE CASCADE,
	item_text VARCHAR(255) NOT NULL,
	item_order INTEGER NOT NULL,
	is_snapshot BOOLEAN NOT NULL DEFAULT FALSE
);

-- 設問用のテーブル作成
CREATE TABLE questions (
	id serial PRIMARY KEY,
	survey_id INTEGER NOT NULL REFERENCES surveys(id) ON DELETE CASCADE,
	answer_pattern_id INTEGER REFERENCES answer_patterns(id) ON DELETE SET NULL,
	question_text text,
	-- ENUM -> VARCHAR
	question_type VARCHAR(50) NOT NULL,
	is_required BOOLEAN NOT NULL DEFAULT FALSE,
	display_order INTEGER NOT NULL DEFAULT 0,
	is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- 回答ヘッダーテーブル
CREATE TABLE responses (
	id serial PRIMARY KEY,
	survey_id INTEGER NOT NULL REFERENCES surveys(id) ON DELETE CASCADE,
	username VARCHAR(50) REFERENCES authentications(username) ON DELETE CASCADE,
	trace_id VARCHAR(255),
	status VARCHAR(50) NOT NULL DEFAULT 'UNVERIFIED',
	created_at timestamp without time zone,
	updated_at timestamp without time zone
);

-- 回答明細テーブル
CREATE TABLE response_details (
	id serial PRIMARY KEY,
	response_id INTEGER NOT NULL REFERENCES responses(id) ON DELETE CASCADE,
	question_id INTEGER NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
	answer_pattern_item_id INTEGER REFERENCES answer_pattern_items(id) ON DELETE SET NULL,
	answer_text text
);

-- アンケートと対象グループの紐づけ
CREATE TABLE survey_target_groups (
	survey_id INTEGER NOT NULL REFERENCES surveys(id) ON DELETE CASCADE,
	group_id INTEGER NOT NULL REFERENCES user_groups(id) ON DELETE CASCADE,
	PRIMARY KEY (survey_id, group_id)
);

-- ユーザーとグループの紐付け
CREATE TABLE user_group_mappings (
	username VARCHAR(50) NOT NULL REFERENCES authentications(username) ON DELETE CASCADE,
	group_id INTEGER NOT NULL REFERENCES user_groups(id) ON DELETE CASCADE,
	PRIMARY KEY (username, group_id)
);