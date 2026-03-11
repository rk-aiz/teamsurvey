package com.github.rk_aiz.teamsurvey.domain.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.model.result.SurveyAggregation;

public interface SurveyResultService {

    /** 全てのアンケート集計を取得します */
    List<SurveyAggregation> findAllSurveyAggregations();

    /**
     * アンケート集計を取得します。
     * 
     * @throws IllegalArgumentException アンケートが存在しない場合
     */
    SurveyAggregation findSurveyAggregationById(Integer surveyId) throws IllegalArgumentException;

    /**
     * ユーザーグループに紐づいたアンケートの集計ヘッダー情報を取得
     * @param userGroupIds
     * @return
     */
    Page<SurveyAggregation> findWithPagingByUserGroups(
            Pageable pageable, List<UserGroup> userGroups);

    /**
     * アンケート集計を取得します。(ユーザーが閲覧可能か確認します。)
     */
    SurveyAggregation findSurveyAggregationByIdAndAccount(Integer surveyId, UserAccount account);


    /**
     * 指定されたアンケートの回答データをCSV形式で生成します。
     */
    void exportToCsv(Integer surveyId, PrintWriter writer) throws IOException;

}