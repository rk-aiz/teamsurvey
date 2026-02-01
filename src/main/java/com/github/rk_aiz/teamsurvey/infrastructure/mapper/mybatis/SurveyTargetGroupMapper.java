package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.SurveyTargetGroupEntity;

@Mapper
public interface SurveyTargetGroupMapper {

    /**
     * 全ての「アンケートと対象グループの紐づけ情報」を取得します
     */
    List<SurveyTargetGroupEntity> selectAll();

    /**
     * 指定されたアンケートIDに対するグループIDを取得します
     */
    List<Integer> selectGroupIdBySurveyId(Integer surveyId);

    /**
     * 指定されたグループIDに対するアンケートIDを取得します
     */
    List<Integer> selectSurveyIdByGroupId(Integer groupId);

    /**
     * 新しい「アンケートと対象グループの紐づけ情報」を登録します
     */
    void insert(SurveyTargetGroupEntity entity);

    /**
     * 「アンケートと対象グループの紐づけ情報」を削除します
     */
    void delete(SurveyTargetGroupEntity entity);

}
