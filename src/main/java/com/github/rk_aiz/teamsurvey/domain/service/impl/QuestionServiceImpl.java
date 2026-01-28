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
    public void saveQuestion(Question question) {

        if (question.getQuestionId() == null) {
            questionRepository.set(question);
        } else {
            questionRepository.add(question);
        }
    }

    @Override
    public void removeQuestion(Integer id) {
        questionRepository.remove(id);
    }

}
