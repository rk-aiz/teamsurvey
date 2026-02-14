package com.github.rk_aiz.teamsurvey.util;

public class ServletUtils {

    private ServletUtils() {
    }

    public static String redirect(Object... objects) {

        StringBuilder sb = new StringBuilder("redirect:");

        for (Object object : objects) {

            if (object == null)
                throw new IllegalArgumentException(
                "リダイレクトパスの一部としてnullが渡されました");

            String str = object.toString();

            if (str.isEmpty())
                throw new IllegalArgumentException(
                "リダイレクトパスの一部として空文字列が渡されました");

            if (str.charAt(0) != '/')
                sb.append("/");

            sb.append(str);
        }

        return sb.toString();
    }

    /**
     * Thymeleafのフラグメントパスを構築するヘルパーを返します。
     * (例: "path/to/view :: fragment")
     * @param basePath テンプレートのベースパス
     * @return フラグメント構築用ヘルパー
     */
    public static Fragment fragment(String basePath) {
        return new Fragment(basePath);
    }

    /**
     * フラグメント構築用ヘルパークラス
     */
    public static class Fragment {
        private final String basePath;

        private Fragment(String basePath) {
            this.basePath = basePath;
        }

        public String target(String fragmentName) {
            return this.basePath + " :: " + fragmentName;
        }
    }
}
