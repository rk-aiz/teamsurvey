package com.github.rk_aiz.teamsurvey.domain.repository;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.Survey;

/**
 * アンケート情報の永続化・検索を行うリポジトリのインターフェース。
 * (実装はインフラ層に配置する)
 */
public interface SurveyRepository {
    List<Survey> findAll();

    /**
     * 指定されたIDに対する質問を取得します
     */
    Survey findById(Integer id);

    /**
     * 新しい質問を登録します
     */
    void add(Survey survey);

    /**
     * 質問を更新します
     */
    void set(Survey survey);

    /**
     * 質問を削除します
     */
    void remove(Integer id);
}