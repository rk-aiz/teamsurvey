package com.github.rk_aiz.teamsurvey.application.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.service.UserGroupService;
import com.github.rk_aiz.teamsurvey.domain.type.Authority;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * CSVファイルからデモデータを読み込み、データベースに登録するローダークラス
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CsvDemoDataLoader {

    private final UserGroupService userGroupService;
    private final JdbcTemplate jdbcTemplate;

    public void loadAll() {
        // 順序を考慮してロード
        loadUserGroups();
        // loadUsers();
        // loadSurveys();
        updateSequences();
    }

    private void loadUserGroups() {
        log.info("Loading UserGroups from CSV...");
        List<String[]> lines = readCsv("demo-data/user_groups.csv");
        
        for (String[] cols : lines) {
            // id, name, authority, isDeletable
            if (cols.length < 4) continue;
            
            try {
                Integer id = Integer.parseInt(cols[0].trim());
                String name = cols[1].trim();
                Authority authority = Authority.valueOf(cols[2].trim());
                boolean isSystemGroup = Boolean.parseBoolean(cols[3].trim());

                // ID指定で保存するためにJdbcTemplateを使用
                // ON CONFLICT (id) DO UPDATE ... を使い、既存の場合は更新、なければ作成
                String sql = """
                        INSERT INTO user_groups (id, group_name, authority, is_system_group) 
                        VALUES (?, ?, ?, ?)
                        ON CONFLICT (id) DO UPDATE SET group_name = EXCLUDED.group_name, authority = EXCLUDED.authority, is_system_group = EXCLUDED.is_system_group
                """;

                jdbcTemplate.update(sql, id, name, authority.name(), isSystemGroup);
                log.info("Loaded UserGroup: id={}, name={}", id, name);
            } catch (Exception e) {
                log.error("Failed to parse user group line: " + Arrays.toString(cols), e);
            }
        }
    }

    private List<String[]> readCsv(String path) {
        List<String[]> records = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(path);
        
        if (!resource.exists()) {
            log.warn("CSV file not found: {}", path);
            return records;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            
            String line;
            // ヘッダー行スキップなどのロジックが必要ならここに追加
            while ((line = br.readLine()) != null) {
                if (!StringUtils.hasText(line) || line.startsWith("#")) continue;
                records.add(line.split(","));
            }
        } catch (IOException e) {
            log.error("Failed to read CSV file: {}", path, e);
        }
        return records;
    }

    /**
     * ID手動挿入後にシーケンスの整合性を取るための処理
     */
    private void updateSequences() {
        try {
            // user_groupテーブルのIDシーケンスを最大値に合わせる (テーブル名_id_seq という命名規則を想定)
            jdbcTemplate.execute("SELECT setval(pg_get_serial_sequence('user_groups', 'id'), COALESCE((SELECT MAX(id) FROM user_groups), 1))");
            log.info("Updated database sequences.");
        } catch (Exception e) {
            log.warn("Failed to update sequences. If using IDENTITY/SERIAL, manual ID insertion might cause conflicts later.", e);
        }
    }
}