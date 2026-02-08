package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.service.QuestionService;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public List<Question> findAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Question findQuestionById(Integer id) {
        return questionRepository.findById(id);
    }

    @Override
    public List<Question> findQuestionsBySurveyId(Integer surveyId) {
        return questionRepository.findBySurveyId(surveyId);
    }

    @Override
    public boolean saveQuestion(Question question) {
        if (question.getId() != null) {
            return questionRepository.set(question);
        } else {
            return questionRepository.add(question);
        }
    }

    @Override
    public boolean removeQuestion(Integer id) {
        return questionRepository.remove(id);
    }

    @Override
    public boolean removeQuestionBySurveyId(Integer surveyId) {
        return questionRepository.removeBySurveyId(surveyId);
    }

}
