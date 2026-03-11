package com.github.rk_aiz.teamsurvey.infrastructure.repository_impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.model.result.SurveyAggregation;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.impl.SurveyResultRepositoryImpl;

// H2データベースをPostgreSQL互換モードで起動するように設定を追加
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.sql.init.mode=never"
})
@Transactional // テストメソッド終了後にDBの変更をロールバック(他のテストに影響を与えないため)
class SurveyResultRepositoryImplTest {

    @Autowired
    private SurveyResultRepositoryImpl surveyResultRepository;

    @Test
    @DisplayName("集計結果リストが正しく取得されること(DB接続あり)")
    @Sql(scripts = "/test-schema.sql")
    @Sql(scripts = "/test-data.sql") // テスト実行前にSQLを実行してデータを初期化
    void findWithPagingByUserGroupIds_ReturnsListOfAggregations() {

        List<SurveyAggregation> list = surveyResultRepository.findWithPagingByUserGroupIds(
            0, 10, List.of(1, 2));

        assertEquals(1, list.size());
    }
}