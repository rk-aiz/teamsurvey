package com.github.rk_aiz.teamsurvey.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.repository.SurveyRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    /**
     * アンケート詳細を取得します。
     * 
     * @param surveyId アンケートID
     * @return アンケートドメインモデル
     * @throws IllegalArgumentException アンケートが存在しない場合
     */
    public Survey getSurvey(Integer surveyId) {
        // Serviceは「どうやってDBから取るか」や「Entityの変換」を知らなくていい
        // ただ「Repositoryからドメインモデルが返ってくる」ことだけを知っている
        Survey survey = surveyRepository.findById(surveyId);

        if (survey == null) {
            throw new IllegalArgumentException("指定されたアンケートが見つかりません: " + surveyId);
        }

        return survey;
    }
}