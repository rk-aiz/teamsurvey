package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.rk_aiz.teamsurvey.infrastructure.entity.ResponseEntity;

@Mapper
public interface ResponseMapper {

    /**
     * 全ての回答を取得します
     */
    List<ResponseEntity> selectAll();

    /**
     * 指定されたIDに対する回答を取得します
     */
    ResponseEntity selectById(@Param("id") Integer id);

    /**
     * 指定されたユーザー名に対する回答を取得します
     */
    List<ResponseEntity> selectByUsername(String username);

    /**
     * 新しい回答を登録します
     */
    void insert(ResponseEntity responseEntity);

    /**
     * 回答を更新します
     */
    void update(ResponseEntity responseEntity);

    /**
     * 回答を削除します
     */
    void delete(@Param("id") Integer id);

}
