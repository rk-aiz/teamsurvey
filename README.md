# TeamSurvey

### 概要 (Overview)

    社内やチーム内での利用を想定した、オンラインアンケートシステムです。
    管理者がアンケートを作成・配布し、ユーザーが回答、結果を集計する一連のフローを提供します。

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

- **Java 21** / **Spring Boot 4.02**
- **PostgreSQL 18**
- **MyBatis**
- **Thymeleaf**
- **Docker** / **Docker Compose**

## ドキュメント (Documentation)

本プロジェクトの要件定義や設計ついては、以下のドキュメントを参照してください。

- **[要件定義書](./docs/要件定義書.md)**

- **[基本設計書](./docs/design/基本設計書.xlsx)**

- **[データベース定義書](./docs/schema/DB定義書.xlsx)**
