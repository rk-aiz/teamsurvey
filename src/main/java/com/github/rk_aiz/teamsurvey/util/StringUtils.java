package com.github.rk_aiz.teamsurvey.util;

/**
 * ドメイン層で使用する文字列操作ユーティリティ。
 * フレームワークに依存しない純粋なJava実装。
 */
public class StringUtils {

    /**
     * 文字列がnullでも空文字(空白含む)でもないことを判定します。
     * 
     * @param str 判定対象の文字列
     * @return true: テキストが含まれている, false: nullまたは空白のみ
     */
    public static boolean hasText(String str) {
        return str != null && !str.isBlank();
    }

    /**
     * 空白のトリムを行います。
     * トリム後、空の文字列の場合はnullを返します。
     */
    public static String trim(String str) {
        String trimmed = str.trim();
        if (trimmed.isEmpty()) {
            return null;
        } else {
            return trimmed;
        }
    }
}