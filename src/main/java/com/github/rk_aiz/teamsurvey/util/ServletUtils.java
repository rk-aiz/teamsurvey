package com.github.rk_aiz.teamsurvey.util;

public class ServletUtils {

    private ServletUtils() {
    }

    public static String redirect(Object... objects) {

        StringBuilder sb = new StringBuilder("redirect:");

        for (Object object : objects) {

            if (object == null)
                continue;

            String str = object.toString();

            if (str.isEmpty())
                continue;

            if (str.charAt(0) != '/')
                sb.append("/");

            sb.append(str);
        }

        return sb.toString();
    }
}
