package com.github.rk_aiz.teamsurvey.infrastructure.repository_impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.repository.QuestionRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.QuestionEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.QuestionMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {

    private final QuestionMapper questionMapper;

    @Override
    public List<Question> findAll() {
        return this.questionMapper.selectAll()
                .stream().map(QuestionEntity::toModel).toList();
    }

    @Override
    public Question findById(Integer id) {
        return this.questionMapper.selectById(id).toModel();
    }

    @Override
    public List<Question> findBySurveyId(Integer surveyId) {
        return this.questionMapper.selectBySurveyId(surveyId)
                .stream().map(QuestionEntity::toModel).toList();
    }

    @Override
    public void add(Question question) {
        QuestionEntity entity = QuestionEntity.fromModel(question);
        questionMapper.insert(entity);
        // 自動採番されたIDをドメインモデルに反映
        question.setQuestionId(entity.getId());
    }

    @Override
    public void set(Question question) {
        QuestionEntity entity = QuestionEntity.fromModel(question);
        questionMapper.update(entity);
    }

    @Override
    public void remove(Integer id) {
        questionMapper.delete(id);
    }

}
