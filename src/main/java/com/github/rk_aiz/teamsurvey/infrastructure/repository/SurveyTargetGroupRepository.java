package com.github.rk_aiz.teamsurvey.infrastructure.repository;

import java.util.List;

/**
 * 「アンケートとグループの紐づけ情報」永続化・検索を行うリポジトリのインターフェース。
 */
public interface SurveyTargetGroupRepository {

    List<Integer> findByGroupId(Integer groupId);

    List<Integer> findBySurveyId(Integer surveyId);

    /**
     * 新しい「アンケートとグループの紐づけ情報」を登録します
     */
    boolean add(Integer surveyId, Integer groupId);

    /*
     * バルクインサート
     */
    boolean add(Integer surveyId, List<Integer> groupIds);

    /**
     * 「アンケートとグループの紐づけ情報」を削除します
     */
    boolean remove(Integer surveyId, Integer groupId);

    /**
     * 「指定されたアンケートIDのグループの紐づけ情報」を削除します
     */
    
    boolean removeBySurveyId(Integer surveyId);
}