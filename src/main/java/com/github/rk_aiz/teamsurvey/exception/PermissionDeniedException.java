package com.github.rk_aiz.teamsurvey.exception;

/**
 * 権限がないリソースへのアクセスや操作が行われた場合にスローされる例外。
 */
public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException(String message) {
        super(message);
    }
}