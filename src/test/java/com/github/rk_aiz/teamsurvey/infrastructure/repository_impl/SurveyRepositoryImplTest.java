package com.github.rk_aiz.teamsurvey.infrastructure.repository_impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.model.question.SingleChoiceQuestion;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.SurveyEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.SurveyMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.QuestionRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.impl.SurveyRepositoryImpl;

@ExtendWith(MockitoExtension.class)
class SurveyRepositoryImplTest {

    @Mock
    private SurveyMapper surveyMapper;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private SurveyRepositoryImpl surveyRepository;

    private SurveyEntity surveyEntity;

    @BeforeEach
    void setUp() {
        surveyEntity = SurveyEntity.builder()
                .id(1)
                .title("Test Survey")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void findAll_ReturnsListOfSurveys() {
        when(surveyMapper.selectAll()).thenReturn(List.of(surveyEntity));

        List<Survey> result = surveyRepository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(surveyEntity.getId(), result.get(0).getSurveyId());
        verify(surveyMapper).selectAll();
    }

    @Test
    void findById_WhenExists_ReturnsSurveyWithQuestions() {
        Integer id = 1;
        when(surveyMapper.selectById(id)).thenReturn(surveyEntity);

        Question question = new SingleChoiceQuestion();
        question.setQuestionId(10);
        when(questionRepository.findBySurveyId(id)).thenReturn(List.of(question));

        Survey result = surveyRepository.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getSurveyId());
        assertEquals(1, result.getQuestions().size());
        assertEquals(10, result.getQuestions().iterator().next().getQuestionId());

        verify(surveyMapper).selectById(id);
        verify(questionRepository).findBySurveyId(id);
    }

    @Test
    void findById_WhenNotExists_ReturnsNull() {
        Integer id = 99;
        when(surveyMapper.selectById(id)).thenReturn(null);

        Survey result = surveyRepository.findById(id);

        assertNull(result);
        verify(surveyMapper).selectById(id);
        verify(questionRepository, never()).findBySurveyId(any());
    }

    @Test
    void add_SavesSurveyAndQuestions() {
        Survey newSurvey = Survey.builder()
                .title("New Survey")
                .questions(new HashMap<>())
                .build();

        Question newQuestion = new SingleChoiceQuestion(); // ID is null
        newSurvey.getQuestions().add(newQuestion);

        // Mock mapper insert to set ID
        doAnswer(invocation -> {
            SurveyEntity entity = invocation.getArgument(0);
            entity.setId(100);
            return null;
        }).when(surveyMapper).insert(any(SurveyEntity.class));

        surveyRepository.add(newSurvey);

        assertEquals(100, newSurvey.getSurveyId());
        assertEquals(100, newQuestion.getSurveyId()); // Check parent ID set

        verify(surveyMapper).insert(any(SurveyEntity.class));
        verify(questionRepository).add(newQuestion);
    }

    @Test
    void set_UpdatesSurveyAndQuestions() {
        Survey existingSurvey = Survey.builder()
                .surveyId(1)
                .title("Updated Survey")
                .questions(new HashMap<>())
                .build();

        Question existingQuestion = new SingleChoiceQuestion();
        existingQuestion.setQuestionId(10);

        Question newQuestion = new SingleChoiceQuestion(); // ID null

        existingSurvey.getQuestions().add(existingQuestion);
        existingSurvey.getQuestions().add(newQuestion);

        surveyRepository.set(existingSurvey);

        verify(surveyMapper).update(any(SurveyEntity.class));

        // existing question -> set
        verify(questionRepository).set(existingQuestion);
        assertEquals(1, existingQuestion.getSurveyId());

        // new question -> add
        verify(questionRepository).add(newQuestion);
        assertEquals(1, newQuestion.getSurveyId());
    }

    @Test
    void remove_DeletesSurvey() {
        Integer id = 1;
        surveyRepository.remove(id);
        verify(surveyMapper).delete(id);
    }
}