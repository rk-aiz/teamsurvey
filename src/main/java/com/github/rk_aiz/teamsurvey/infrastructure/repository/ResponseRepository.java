package com.github.rk_aiz.teamsurvey.infrastructure.repository;

import java.util.List;

import com.github.rk_aiz.teamsurvey.domain.model.Response;

/**
 * 回答情報の永続化・検索を行うリポジトリのインターフェース。
 */
public interface ResponseRepository {

    List<Response> findAll();

    /**
     * 指定されたIDに対する回答を取得します
     */
    Response findById(Integer id);

    /**
     * 指定されたユーザー名に対する回答を取得します
     */
    List<Response> findByUsername(String username);

    /**
     * 新しい回答を登録します
     */
    void add(Response response);

    /**
     * 回答を更新します
     */
    void set(Response response);

    /**
     * 回答を削除します
     */
    void remove(Integer id);
}