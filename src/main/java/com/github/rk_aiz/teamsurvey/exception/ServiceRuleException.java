package com.github.rk_aiz.teamsurvey.exception;

/**
 * サービス層での処理実行ルールに違反した場合(ユーザー通知が必要なエラー)にスローされる例外
 */
public class ServiceRuleException extends RuntimeException {
    public ServiceRuleException(String message) {
        super(message);
    }
}