package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.rk_aiz.teamsurvey.infrastructure.entity.SurveyEntity;

@Mapper
public interface SurveyMapper {

    /**
     * 全てのアンケート(ヘッダー)を取得します
     */
    List<SurveyEntity> selectAll();

    /**
     * 指定されたIDに対するアンケートを取得します
     */
    SurveyEntity selectById(@Param("id") Integer id);

    /**
     * 指定されたユーザー名に対するアンケートを取得します
     */
    List<SurveyEntity> selectByUsername(String username);

    /**
     * 指定されたユーザーが、指定されたアンケートの対象化を確認します
     */
    boolean existsMappingByIdAndUsername(
            @Param("surveyId") Integer surveyId, @Param("username") String username);

    /**
     * 新しいアンケートを登録します
     */
    void insert(SurveyEntity surveyEntity);

    /**
     * アンケートを更新します
     */
    void update(SurveyEntity surveyEntity);

    /**
     * アンケートを削除します
     */
    void delete(@Param("id") Integer id);

}
