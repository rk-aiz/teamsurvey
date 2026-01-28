package com.github.rk_aiz.teamsurvey.infrastructure.repository;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;

public interface AnswerOptionRepository {
    /**
     * 全ての回答パターンを取得します
     * (選択肢リストは取得しません)
     */
    List<AnswerOption> findAll();

    /**
     * 全ての回答パターンを取得します（選択肢リスト付き）
     */
    List<AnswerOption> selectAllWithItems();

    /**
     * 指定されたIDに対する回答パターンを取得します
     * (紐づく選択肢リストも取得します)
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
