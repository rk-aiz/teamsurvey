package com.github.rk_aiz.teamsurvey.util;

import java.util.Map;
import java.util.function.Consumer;

public interface CsvRowConsumer extends Consumer<Map<String, Object>> {
    void accept(Map<String, Object> row);
}