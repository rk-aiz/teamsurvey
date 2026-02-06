package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    int insert(@Param("surveyId") Integer surveyId, @Param("groupId") Integer groupId);

    /**
     * バルクインサート
     */
    int insertBulk(@Param("surveyId") Integer surveyId, @Param("groupIds") List<Integer> groupIds);

    /**
     * 「アンケートと対象グループの紐づけ情報」を削除します
     */
    int delete(@Param("surveyId") Integer surveyId, @Param("groupId") Integer groupId);

    /**
     * 「指定されたアンケートIDのグループの紐づけ情報」を削除します
     */
    int deleteBySurveyId(@Param("surveyId") Integer surveyId);

}
