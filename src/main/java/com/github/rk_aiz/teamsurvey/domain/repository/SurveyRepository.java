package com.github.rk_aiz.teamsurvey.domain.repository;

import com.github.rk_aiz.teamsurvey.domain.model.Survey;

/**
 * アンケート情報の永続化・検索を行うリポジトリのインターフェース。
 * (実装はインフラ層に配置する)
 */
public interface SurveyRepository {
    Survey findById(Integer id);
}