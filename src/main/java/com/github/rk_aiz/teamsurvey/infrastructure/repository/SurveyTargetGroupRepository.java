package com.github.rk_aiz.teamsurvey.infrastructure.repository;

import java.util.List;

/**
 * 「アンケートとグループの紐づけ情報」永続化・検索を行うリポジトリのインターフェース。
 */
public interface SurveyTargetGroupRepository {

    List<Integer> findByGroupId(Integer groupId);

    List<Integer> findBySurveyId(Integer surveyId);

    public boolean updateTargetGroups(Integer surveyId, List<Integer> groupIds);

    /**
     * 「アンケートとグループの紐づけ情報」を削除します
     */
    boolean remove(Integer surveyId, Integer groupId);

    /**
     * 「指定されたアンケートIDのグループの紐づけ情報」を削除します
     */

    boolean removeBySurveyId(Integer surveyId);
}