package com.github.rk_aiz.teamsurvey.infrastructure.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.SurveyEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.SurveyMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.QuestionRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.SurveyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SurveyRepositoryImpl implements SurveyRepository {

    private final SurveyMapper surveyMapper;
    private final QuestionRepository questionRepository;

    @Override
    public List<Survey> findAll() {
        return surveyMapper.selectAll().stream().map(SurveyEntity::toModel).toList();
    }

    @Override
    public Survey findById(Integer id) {
        // 1. Header取得
        SurveyEntity entity = surveyMapper.selectById(id);
        if (entity == null)
            return null;

        Survey survey = entity.toModel();

        // 2. Questions取得
        survey.setQuestions(questionRepository.findBySurveyId(id));

        return survey;
    }

    @Override
    public void add(Survey survey) {
        // 1. Header保存
        SurveyEntity entity = SurveyEntity.fromModel(survey);
        surveyMapper.insert(entity);

        // 自動採番されたIDをドメインモデルに反映
        survey.setSurveyId(entity.getId());

        // 2. Questions保存 (新規作成時も質問があれば保存する)
        if (survey.getQuestions() != null) {
            saveQuestions(entity.getId(), survey.getQuestions());
        }
    }

    @Override
    public void set(Survey survey) {
        // 1. Headerの更新
        SurveyEntity entity = SurveyEntity.fromModel(survey);
        surveyMapper.update(entity);

        if (survey.getQuestions() != null) {
            saveQuestions(entity.getId(), survey.getQuestions());
        }
    }

    /**
     * 質問リストを保存するヘルパーメソッド
     */
    private void saveQuestions(Integer surveyId, List<Question> questions) {
        for (Question q : questions) {
            q.setSurveyId(surveyId); // 親IDをセット
            if (q.getQuestionId() == null) {
                questionRepository.add(q);
            } else {
                questionRepository.set(q);
            }
        }
    }

    @Override
    public void remove(Integer id) {
        // 子要素の削除はDBの外部キー制約(ON DELETE CASCADE)に任せる
        surveyMapper.delete(id);
    }

}
