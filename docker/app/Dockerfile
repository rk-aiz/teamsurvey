# ビルドステージ
# Java 21が含まれているGradleイメージを指定します
FROM gradle:jdk21 AS build
WORKDIR /app
COPY . .
# テストをスキップしてビルドを実行
RUN gradle bootJar -x test --no-daemon

# 実行ステージ
# 実行用には軽量なJRE 21 (Alpine Linux版など) を使用
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# ビルドステージで生成されたJARファイルをコピー
# プロジェクト名やバージョンによってJARファイル名が変わる場合があるため、ワイルドカードを使用するか固定名で出力する設定が必要です
# ここでは生成されたJARを app.jar としてコピーします
COPY --from=build /app/build/libs/*.jar app.jar

# ポート8081を公開 (docker-compose.ymlの設定に合わせる)
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
