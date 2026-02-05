package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.rk_aiz.teamsurvey.infrastructure.entity.AnswerPatternItemEntity;


@Mapper
public interface AnswerPatternItemMapper {

    /**
     * パターンIDに紐づく選択肢リストを取得します
     */
    List<AnswerPatternItemEntity> selectByPatternId(Integer patternId);

    /**
     * 新しい選択肢を登録します
     */
    void insert(AnswerPatternItemEntity item);

    /**
     * 選択肢を更新します
     */
    void update(AnswerPatternItemEntity item);

    /**
     * 選択肢を削除します
     */
    void delete(@Param("id") Integer id);

    /**
     * 指定されたパターンIDに紐づく選択肢を全て削除します
     * (パターンの削除時や、一括更新時の洗い替えに使用)
     */
    void deleteByPatternId(@Param("patternId") Integer patternId);
}