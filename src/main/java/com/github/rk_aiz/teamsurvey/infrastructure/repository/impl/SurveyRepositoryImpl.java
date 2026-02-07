package com.github.rk_aiz.teamsurvey.infrastructure.repository.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.SurveyEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.SurveyMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.QuestionRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.SurveyRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.SurveyTargetGroupRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.UserGroupRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SurveyRepositoryImpl implements SurveyRepository {

    private final SurveyMapper surveyMapper;
    private final QuestionRepository questionRepository;
    private final UserGroupRepository userGroupRepository;
    private final SurveyTargetGroupRepository surveyTargetGroupRepository;

    @Override
    public List<Survey> findAll() {
        return this.surveyMapper.selectAll().stream().map(SurveyEntity::toModel).toList();
    }

    @Override
    public Survey findById(Integer id) {
        // 1. Header取得
        SurveyEntity entity = this.surveyMapper.selectById(id);
        if (entity == null)
            return null;

        Survey survey = entity.toModel();

        // 2. Questions取得
        survey.setQuestions(this.questionRepository.findBySurveyId(id));

        // 3. Groups取得
        survey.setTargetGroups(this.userGroupRepository.findBySurveyId(id));

        return survey;
    }

    @Override
    public List<Survey> findByUsername(String username) {
        return this.surveyMapper.selectByUsername(username).stream().map(SurveyEntity::toModel).toList();
    }

    @Override
    public boolean canResponse(Integer surveyId, String username) {
        return this.surveyMapper.existsMappingByIdAndUsername(surveyId, username);
    }

    @Override
    public boolean add(Survey survey) {

        // 1. Header保存
        SurveyEntity entity = SurveyEntity.fromModel(survey);
        boolean ret = this.surveyMapper.insert(entity) > 0;

        // 自動採番されたIDをドメインモデルに反映
        survey.setId(entity.getId());

        // 2. Questions保存 (新規作成時も質問があれば保存する)
        Collection<Question> questions = survey.getQuestions();
        if (questions != null && !questions.isEmpty()) {
            ret &= this.saveQuestions(entity.getId(), questions);
        }

        return ret;
    }

    @Override
    public boolean updateHeader(Survey survey) {
        // 1. Headerの更新
        return 0 < this.surveyMapper.update(SurveyEntity.fromModel(survey));
    }

    /**
     * 質問リストを保存するヘルパーメソッド
     */
    private boolean saveQuestions(Integer surveyId, Collection<Question> questions) {
        boolean ret = true;

        for (Question q : questions) {
            q.setSurveyId(surveyId); // 親IDをセット
            if (q.getId() == null) {
                ret &= this.questionRepository.add(q);
            } else {
                ret &= this.questionRepository.set(q);
            }
        }
        return ret;
    }

    @Override
    public boolean remove(Integer id) {
        // 子要素の削除はDBの外部キー制約(ON DELETE CASCADE)に任せる
        return this.surveyMapper.delete(id) > 0;
    }
}
