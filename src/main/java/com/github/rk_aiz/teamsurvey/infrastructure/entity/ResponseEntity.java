package com.github.rk_aiz.teamsurvey.infrastructure.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.BeanUtils;

import com.github.rk_aiz.teamsurvey.domain.model.Response;
import com.github.rk_aiz.teamsurvey.domain.type.ResultVisibility;
import com.github.rk_aiz.teamsurvey.domain.type.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * surveysテーブルに対応するEntity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEntity {
    /** 主キー */
    private Integer id;
    /** 対象アンケート */
    private Integer surveyId;
    /** 回答者名 */
    private String username;
    /** 追跡情報 */
    private String traceId;
    /** 回答の状態 (有効、無効、テストなど) */
    private ResponseStatus status;
    /** アンケート作成日時 */
    private LocalDateTime createdAt;
    /** アンケート更新日時 */
    private LocalDateTime updatedAt;

    /**
     * Entity -> Domain Model 変換
     */
    public Response toModel() {
        Response model = new Response();
        model.setResponseId(this.id);
        BeanUtils.copyProperties(this, model);
        return model;
    }

    /**
     * Domain Model -> Entity 変換
     */
    public static ResponseEntity fromModel(Response model) {
        ResponseEntity entity = new ResponseEntity();
        entity.setId(model.getResponseId());
        BeanUtils.copyProperties(model, entity);
        return entity;
    }
}
