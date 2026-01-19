package com.github.rk_aiz.teamsurvey.infrastructure.repository_impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.rk_aiz.teamsurvey.domain.model.question.MultipleChoiceQuestion;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.model.question.SingleChoiceQuestion;
import com.github.rk_aiz.teamsurvey.domain.repository.AnswerOptionRepository;
import com.github.rk_aiz.teamsurvey.domain.repository.QuestionRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.QuestionEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.QuestionMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {

    private final QuestionMapper questionMapper;
    private final AnswerOptionRepository answerOptionRepository;


    @Override
    public List<Question> findAll() {
        return this.questionMapper.selectAll()
                .stream().map(QuestionEntity::toModel).toList();
    }

    @Override
    public Question findById(Integer id) {
        QuestionEntity entity = questionMapper.selectById(id);
        if (entity == null) return null;
        
        return convertWithAnswerOption(entity);
    }

    @Override
    public List<Question> findBySurveyId(Integer surveyId) {
        return this.questionMapper.selectBySurveyId(surveyId)
                .stream().map(entity -> this.convertWithAnswerOption(entity)).toList();
    }

    @Override
    public void add(Question question) {
        QuestionEntity entity = QuestionEntity.from(question);
        questionMapper.insert(entity);
        // 自動採番されたIDをドメインモデルに反映
        question.setQuestionId(entity.getId());
    }

    @Override
    public void set(Question question) {
        QuestionEntity entity = QuestionEntity.from(question);
        questionMapper.update(entity);
    }

    @Override
    public void remove(Integer id) {
        questionMapper.delete(id);
    }

    private Question convertWithAnswerOption(QuestionEntity entity) {
        Question question = entity.toModel();
            switch (question.getType()) {
                case RADIO -> {
                    ((SingleChoiceQuestion)question).setAnswerOption(
                            answerOptionRepository.findById(entity.getAnswerPatternId()));
                }
                case CHECKBOX -> {
                    ((MultipleChoiceQuestion)question).setAnswerOption(
                            answerOptionRepository.findById(entity.getAnswerPatternId()));
                }
                default -> {}
            }
        return question;
    }
        

}
