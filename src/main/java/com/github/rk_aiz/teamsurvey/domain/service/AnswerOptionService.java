package com.github.rk_aiz.teamsurvey.domain.service;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;

public interface AnswerOptionService {

    /**
     * 回答選択肢の一覧を取得します。
     */
    List<AnswerOption> findAll();

    /**
     * 回答選択肢の詳細をIDから取得する
     */
    AnswerOption findAnswerOptionById(Integer id);

    public void save(AnswerOption answerOption);

    /**
     * 回答選択肢を削除する
     */
    public void remove(Integer id);
}