# ビルドステージ: GradleとJDKを含むイメージ
FROM gradle:8-jdk17 AS build
WORKDIR /app

# ソースコードをコピーしてビルド (テストはスキップ)
COPY . .
RUN gradle bootJar -x test --no-daemon

# 実行ステージ
FROM eclipse-temurin:17-jre
WORKDIR /app
# Gradleのビルド成果物は build/libs に生成されます
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]