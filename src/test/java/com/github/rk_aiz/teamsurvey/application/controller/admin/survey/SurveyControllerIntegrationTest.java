package com.github.rk_aiz.teamsurvey.application.controller.admin.survey;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.type.ResultVisibility;
import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;

// H2データベースをPostgreSQL互換モードで起動するように設定を追加
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
})
@AutoConfigureMockMvc // MockMvcを自動設定して、HTTPリクエストをシミュレート可能にする
@Transactional // テストメソッド終了後にDBの変更をロールバック(他のテストに影響を与えないため)
class SurveyControllerIntegrationTest {

    private static final String LIST_REQUEST = "/admin/survey/list";
    private static final String SAVE_REQUEST = "/admin/survey/save";
    private static final String EDIT_REQUEST = "/admin/survey/edit";
    private static final String DETAIL_REQUEST = "/admin/survey/detail";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("アンケート一覧画面が正常に表示されること(DB接続あり)")
    @WithMockUser(username = "admin", authorities = "ADMIN")
    @Sql(scripts = "/test-schema.sql")
    @Sql(scripts = "/test-data.sql") // テスト実行前にSQLを実行してデータを初期化
    void list_ReturnsOk() throws Exception {
        // Service -> Repository -> H2 Database まで貫通して実行されます。
        // test-data.sql で投入したデータが正しく取得できるか検証します。

        mockMvc.perform(get(LIST_REQUEST))
                .andExpect(status().isOk())
                .andExpect(view().name(SurveyController.SURVEY_LIST))
                .andExpect(model().attributeExists("surveys"))
                // リストのサイズが2件であることを検証
                .andExpect(model().attribute("surveys", hasSize(2)))
                // リストの中に「テストアンケートA」というタイトルの要素が含まれているか検証
                .andExpect(model().attribute("surveys", hasItem(
                        hasProperty("title", is("テストアンケートA")))));
    }

    @Test
    @DisplayName("テストクラス内で直接SQL(DML)を実行する例")
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void directSql_ReturnsOk() throws Exception {
        // JdbcTemplateを使って直接INSERT文を実行
        jdbcTemplate.update(
                "INSERT INTO surveys (title, status, result_visibility, created_at, updated_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                "直接SQLで登録したアンケート", "DRAFT", "ADMIN_ONLY");

        mockMvc.perform(get(LIST_REQUEST))
                .andExpect(status().isOk())
                .andExpect(model().attribute("surveys", hasItem(
                        hasProperty("title", is("直接SQLで登録したアンケート")))));
    }

    @Test
    @DisplayName("コントローラー経由で保存し、その結果を一覧で確認する(一連のフロー)")
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void save_AndThenList_ReturnsOk() throws Exception {
        // 1. コントローラーにPOSTして保存 (DRAFTステータス)
        // フォームから送信されるパラメータを .param() で再現します
        mockMvc.perform(post(SAVE_REQUEST)
                .with(csrf()) // CSRFトークン
                .param("title", "コントローラー結合テスト用アンケート")
                .param("status", "DRAFT")
                .param("resultVisibility", "ADMIN_ONLY")
                .param("isNew", "true"))
                .andExpect(status().is3xxRedirection()); // 成功すると詳細画面へリダイレクトされる

        // 2. 一覧画面を取得して、保存されたデータが存在するか確認
        mockMvc.perform(get(LIST_REQUEST))
                .andExpect(status().isOk())
                .andExpect(model().attribute("surveys", hasItem(
                        allOf(
                                hasProperty("title", is("コントローラー結合テスト用アンケート")),
                                hasProperty("status", is(SurveyStatus.DRAFT)),
                                hasProperty("resultVisibility", is(ResultVisibility.ADMIN_ONLY))))));
    }

    @Test
    @DisplayName("バリデーションエラー(タイトル未入力)時にエラーメッセージが表示されること")
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void save_InvalidForm_ReturnsEditViewWithErrors() throws Exception {
        // When: タイトルが空のリクエストを送信
        mockMvc.perform(post(SAVE_REQUEST)
                .with(csrf())
                .param("title", "") // バリデーションエラーになる値(空文字)
                .param("status", "DRAFT")
                .param("resultVisibility", "ADMIN_ONLY")
                .param("isNew", "true"))
                .andExpect(status().isOk()) // リダイレクトされず、画面再表示(200)になるはず
                .andExpect(view().name(SurveyController.SURVEY_EDIT)) // 編集画面に戻る
                .andExpect(model().hasErrors()) // エラーが含まれていること
                .andExpect(model().attributeHasFieldErrors("surveyForm", "title")); // titleフィールドにエラーがあること
    }

    @Test
    @DisplayName("設問付きでアンケートを保存し、詳細画面で内容を確認する")
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void saveWithQuestions_AndThenDetail_ReturnsOk() throws Exception {
        // 1. 設問付きで保存 (TEXT形式とRADIO形式)
        jdbcTemplate.update(
            "INSERT INTO answer_patterns (id, pattern_name) VALUES (?, ?)",
        1, "難易度(5段階)");

        var resultActions = mockMvc.perform(post(SAVE_REQUEST)
                .with(csrf())
                .param("title", "設問付きアンケートテスト")
                .param("status", "DRAFT")
                .param("resultVisibility", "ADMIN_ONLY")
                .param("isNew", "true")
                // 1問目: 自由記述
                .param("questionForms[0].text", "感想を教えてください")
                .param("questionForms[0].type", "TEXT")
                .param("questionForms[0].required", "false")
                // 2問目: 単一選択 (AnswerOption ID=1)
                .param("questionForms[1].text", "難易度はどうでしたか？")
                .param("questionForms[1].type", "RADIO")
                .param("questionForms[1].answerOptionId", "1")
                .param("questionForms[1].required", "true"))
                .andExpect(status().is3xxRedirection());

        // リダイレクト先(詳細画面)のURLを取得
        String redirectedUrl = resultActions.andReturn().getResponse().getRedirectedUrl();

        // 2. 詳細画面を取得して検証
        mockMvc.perform(get(redirectedUrl))
                .andExpect(status().isOk())
                .andExpect(view().name(SurveyController.SURVEY_DETAIL))
                .andExpect(model().attributeExists("survey"))
                // Surveyの検証
                .andExpect(model().attribute("survey", hasProperty("title", is("設問付きアンケートテスト"))))
                // Questionsの検証
                .andExpect(model().attribute("survey", hasProperty("questions", hasSize(2))))
                .andExpect(model().attribute("survey", hasProperty("questions", hasItem(
                        allOf(
                                hasProperty("text", is("感想を教えてください")),
                                hasProperty("required", is(false))
                        )
                ))))
                .andExpect(model().attribute("survey", hasProperty("questions", hasItem(
                        allOf(
                                hasProperty("text", is("難易度はどうでしたか？")),
                                hasProperty("required", is(true))
                        )
                ))));
    }
}