package com.github.rk_aiz.teamsurvey.domain.service;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.question.Question;

public interface QuestionService {

    /** 全ての質問を取得します */
    List<Question> findAllQuestions();

    /** 指定されたアンケートIDに紐づく質問一覧を取得します */
    List<Question> findQuestionsBySurveyId(Integer surveyId);

    /** 指定されたIDの質問を取得します */
    Question findQuestionById(Integer id);

    /** 質問を保存（新規・更新）します */
    void saveQuestion(Question question);

    /** 質問を削除します */
    void removeQuestion(Integer id);
}