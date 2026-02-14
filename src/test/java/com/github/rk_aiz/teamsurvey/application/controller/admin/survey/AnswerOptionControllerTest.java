package com.github.rk_aiz.teamsurvey.application.controller.admin.survey;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.github.rk_aiz.teamsurvey.application.form.AnswerOptionForm;
import com.github.rk_aiz.teamsurvey.domain.service.AnswerOptionService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(AnswerOptionController.class)
@WithMockUser(username = "admin", roles = "ADMIN")
class AnswerOptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnswerOptionService answerOptionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("一覧フラグメントが正常に取得できること")
    void getListFragment_ReturnsListView() throws Exception {
        // Given: サービスが空のリストを返すように設定
        given(answerOptionService.findAll()).willReturn(Collections.emptyList());

        // When & Then: GETリクエストを実行し、View名とModelを検証
        mockMvc.perform(get("/admin/pattern/fragment/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/survey/pattern_fragments :: list"))
                .andExpect(model().attributeExists("answerOptions"));
    }

    @Test
    @DisplayName("新規作成フォームフラグメントが正常に取得できること")
    void getFormFragment_New_ReturnsFormView() throws Exception {
        // When & Then
        mockMvc.perform(get("/admin/pattern/fragment/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/survey/pattern_fragments :: form"))
                .andExpect(model().attributeExists("answerOptionForm"));
    }

    @Test
    @DisplayName("回答パターンを保存できること")
    void save_ValidForm_ReturnsOk() throws Exception {
        // Given
        AnswerOptionForm form = new AnswerOptionForm();
        form.setName("テストパターン");

        // When & Then: JSONをPOSTして検証
        mockMvc.perform(post("/admin/pattern/fragment/save")
                .with(csrf()) // Spring Securityが有効な場合、CSRFトークンが必要
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("回答パターンを保存しました。"));

        // サービスが呼ばれたことを検証
        verify(answerOptionService).save(any());
    }

    @Test
    @DisplayName("JSON形式で一覧を取得できること")
    void getListJson_ReturnsJsonList() throws Exception {
        // Given
        given(answerOptionService.findAll()).willReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/admin/pattern/fragment/list-json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }
}