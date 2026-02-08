package com.github.rk_aiz.teamsurvey.domain.exception;

/**
 * システムの整合性が取れない、あるいは想定外の状態など、
 * 続行不可能なクリティカルなエラーを表す例外。
 * ユーザーには詳細を見せず、システムエラーとして扱うことを想定しています。
 */
public class SystemCriticalException extends RuntimeException {
    public SystemCriticalException(String message) {
        super(message);
    }

    public SystemCriticalException(String message, Throwable cause) {
        super(message, cause);
    }
}