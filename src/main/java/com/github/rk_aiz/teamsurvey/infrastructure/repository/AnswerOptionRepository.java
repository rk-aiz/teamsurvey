package com.github.rk_aiz.teamsurvey.infrastructure.repository;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;

public interface AnswerOptionRepository {
    /**
     * 全ての回答パターンを取得します
     */
    List<AnswerOption> findAll();

    /**
     * 指定されたIDに対する回答パターンを取得します
     */
    AnswerOption findById(Integer id);

    /**
     * 新しい回答パターンを登録します
     */
    void add(AnswerOption answerOption);

    /**
     * 回答パターンを更新します
     */
    void set(AnswerOption answerOption);

    /**
     * 回答パターンを削除します
     */
    void remove(Integer id);
}
