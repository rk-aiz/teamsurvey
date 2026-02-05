package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.rk_aiz.teamsurvey.infrastructure.entity.AnswerPatternEntity;

@Mapper
public interface AnswerPatternMapper {
    /**
     * 全ての回答パターンを取得します
     * (選択肢リストは取得しません)
     */
    List<AnswerPatternEntity> selectAll();

    /**
     * 指定されたIDに対する回答パターンを取得します
     */
    AnswerPatternEntity selectById(@Param("id") Integer id);

    /**
     * 新しい回答パターンを登録します
     */
    void insert(AnswerPatternEntity pattern);

    /**
     * 回答パターンを更新します
     */
    void update(AnswerPatternEntity pattern);

    /**
     * 回答パターンを削除します
     */
    void delete(@Param("id") Integer id);

}
