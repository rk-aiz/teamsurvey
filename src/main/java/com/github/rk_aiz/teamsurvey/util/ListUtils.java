package com.github.rk_aiz.teamsurvey.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    private ListUtils() {
    }

    /**
     * リストを指定されたサイズで分割します。
     * (MyBatisのバルクインサート用バッチ作成などに使う用)
     */
    public static <T> List<List<T>> partition(List<T> list, int size) {
        if (list == null) {
            return new ArrayList<>();
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }

        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            // subListを使って切り出す
            // 最後の端数処理も Math.min で対応
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }
}