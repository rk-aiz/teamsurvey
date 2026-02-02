package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.rk_aiz.teamsurvey.infrastructure.entity.ResponseDetailEntity;

@Mapper
public interface ResponseDetailMapper {

    /**
     * 全ての回答詳細を取得します
     */
    List<ResponseDetailEntity> selectAll();

    /**
     * 指定されたIDに対する回答詳細を取得します
     */
    ResponseDetailEntity selectById(@Param("id") Integer id);

    /**
     * 指定された回答IDに対する回答詳細を取得します
     */
    List<ResponseDetailEntity> selectByResponseId(
            @Param("responseId") Integer responseId);

    /**
     * 新しい回答詳細を登録します
     */
    void insert(ResponseDetailEntity responseDetailEntity);

    /**
     * 回答詳細を更新します
     */
    void update(ResponseDetailEntity responseDetailEntity);

    /**
     * 回答詳細を削除します
     */
    void delete(@Param("id") Integer id);

    /**
     * 指定された回答IDに対する回答詳細を削除します
     */
    void deleteByResponseId(@Param("responseId") Integer responseId);

}
