package com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.github.rk_aiz.teamsurvey.infrastructure.entity.AnswerPatternItemEntity;


@Mapper
public interface AnswerPatternItemMapper {

    /**
     * パターンIDに紐づく選択肢リストを取得します
     */
    @Select("SELECT * FROM answer_pattern_items WHERE answer_pattern_id = #{patternId} ORDER BY item_order")
    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "answerPatternId", column = "answer_pattern_id"),
            @Result(property = "itemText", column = "item_text"),
            @Result(property = "itemOrder", column = "item_order")
    })
    List<AnswerPatternItemEntity> selectByPatternId(Integer patternId);

    /**
     * 新しい選択肢を登録します
     */
    @Insert("INSERT INTO answer_pattern_items (answer_pattern_id, item_text, item_order) VALUES (#{answerPatternId}, #{itemText}, #{itemOrder})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(AnswerPatternItemEntity item);

    /**
     * 選択肢を更新します
     */
    @Update("UPDATE answer_pattern_items SET answer_pattern_id = #{answerPatternId}, item_text = #{itemText}, item_order = #{itemOrder} WHERE id = #{id}")
    void update(AnswerPatternItemEntity item);

    /**
     * 選択肢を削除します
     */
    @Delete("DELETE FROM answer_pattern_items WHERE id = #{id}")
    void delete(@Param("id") Integer id);

    /**
     * 指定されたパターンIDに紐づく選択肢を全て削除します
     * (パターンの削除時や、一括更新時の洗い替えに使用)
     */
    @Delete("DELETE FROM answer_pattern_items WHERE answer_pattern_id = #{patternId}")
    void deleteByPatternId(@Param("patternId") Integer patternId);
}