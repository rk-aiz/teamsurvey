# TeamSurvey

### 概要 (Overview)

    社内やチーム内での利用を想定した、オンラインアンケートシステムです。
    管理者がアンケートを作成・配布し、ユーザーが回答、結果を集計する一連のフローを提供します。
    Docker環境での容易な構築と、レイヤードアーキテクチャによる保守性の高い設計を採用しています。

### セットアップ (Setup)

Docker環境があれば、以下の手順でアプリケーションとデータベース、ドキュメント生成ツールを起動できます。

```bash
# 1. リポジトリのクローン
git clone https://github.com/rk-aiz/teamsurvey.git
cd teamsurvey

# 2. コンテナのビルド＆起動
docker compose up -d

# 3. アクセス
# アプリケーション: http://localhost:8080
# DB定義書: docs/schema/index.html (ローカルファイルとして閲覧)

```

## ディレクトリ構造 (Directory Structure)

プロジェクトの全体構造は以下の通りです。
ソースコードだけでなく、環境構築用のDocker設定やドキュメント類もリポジトリ内で管理しています。

```text
teamsurvey/
├── docker/ ........................ Docker環境構築用ファイル
│   ├── app/ ....................... Javaアプリ用Dockerfile
│   └── db/ ........................ DB初期設定など
│
├── docs/ .......................... プロジェクトドキュメント
│   ├── schema/ .................... DB定義書 (SchemaSpy自動生成出力先)
│   └── design/ .................... 設計等の詳細資料
│
├── src/main/java/com/github/rk_aiz/teamsurvey/
│   ├── presentation/ .............. [Web層] Controller, Form
│   ├── application/ ............... [アプリ層] Service (ユースケース)
│   ├── domain/ .................... [ドメイン層] Model
│   └── infrastructure/ ............ [インフラ層] Entity, Mapper
│
├── compose.yaml ................... Docker Compose設定ファイル
└── build.gradle ........................ Gradle依存関係定義

```

## 技術スタック (Tech Stack)

- **Java 21** / **Spring Boot 4.01**
- **PostgreSQL 18**
- **MyBatis**
- **Thymeleaf**
- **Docker** / **Docker Compose**

## ドキュメント (Documentation)

本プロジェクトの要件定義や設計ついては、以下のドキュメントを参照してください。

- **[データベース定義書](./docs/schema/index.html)**
- SchemaSpyによる自動生成ドキュメント

- **[設計思想とアピールポイント](./docs/design/architecture.md)**
- レイヤードアーキテクチャの採用理由
- 管理者/ユーザー機能の分離設計
- 効率的なDBアクセスの工夫
