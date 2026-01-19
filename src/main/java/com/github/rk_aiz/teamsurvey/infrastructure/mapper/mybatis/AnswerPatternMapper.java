package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.rk_aiz.teamsurvey.infrastructure.entity.AnswerPatternEntity;

@Mapper
public interface AnswerPatternMapper {
    /**
     * 全ての回答パターンを取得します
     * (選択肢リストは取得しません)
     */
    @Select("SELECT * FROM answer_patterns ORDER BY id")
    @Results(id = "AnswerPatternMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "patternName", column = "pattern_name"),
            @Result(property = "isDeleted", column = "is_deleted")
    })
    List<AnswerPatternEntity> selectAll();

    /**
     * 指定されたIDに対する回答パターンを取得します
     * (紐づく選択肢リストも取得します)
     */
    @Select("SELECT * FROM answer_patterns WHERE id = #{id}")
    @ResultMap("AnswerPatternWithItemsMap")
    AnswerPatternEntity selectById(@Param("id") Integer id);

    /**
     * 新しい回答パターンを登録します
     */
    @Insert("INSERT INTO answer_patterns (pattern_name) VALUES (#{patternName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(AnswerPatternEntity pattern);

    /**
     * 回答パターンを更新します
     */
    @Update("UPDATE answer_patterns SET pattern_name = #{patternName} WHERE id = #{id}")
    void update(AnswerPatternEntity pattern);

    /**
     * 回答パターンを削除します
     */
    @Delete("DELETE FROM answer_patterns WHERE id = #{id}")
    void delete(@Param("id") Integer id);

}
