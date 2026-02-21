package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.model.result.SurveyAggregation;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyResultService;
import com.github.rk_aiz.teamsurvey.exception.PermissionDeniedException;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.SurveyResultRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyResultServiceImpl implements SurveyResultService {

    private final SurveyResultRepository surveyResultRepository;

    @Override
    public List<SurveyAggregation> findAllSurveyAggregations() {
        return surveyResultRepository.findAll();
    }

    @Override
    public SurveyAggregation findSurveyAggregationById(Integer surveyId) throws IllegalArgumentException {
        return surveyResultRepository.findBySurveyId(surveyId);
    }

    /**
     * ユーザーグループに紐づいたアンケートの集計ヘッダー情報を取得
     */
    @Override
    public Page<SurveyAggregation> findWithPagingByUserGroups(
            Pageable pageable, List<UserGroup> userGroups) {

        List<Integer> userGroupIds = userGroups.stream().map(UserGroup::getId).toList();

        if (userGroupIds.isEmpty()) {
            return Page.empty();
        }

        // 総件数の取得
        long total = surveyResultRepository.countByUserGroupIds(userGroupIds);
        List<SurveyAggregation> aggregations;
        if (total > 0) {
            aggregations = surveyResultRepository.findWithPagingByUserGroupIds(
                    pageable.getOffset(), 
                    pageable.getPageSize(), 
                    userGroupIds);
        } else {
            aggregations = Collections.emptyList();
        }

        return new PageImpl<>(aggregations, pageable, total);
    }

    @Override
    public void exportToCsv(Integer surveyId, PrintWriter writer) throws IOException {
        try (CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            // ヘッダー順序を保持するためのコンテナ (AtomicReferenceでラップしてラムダ内から変更可能にする)
            final AtomicReference<List<String>> headersRef = new AtomicReference<>();

            surveyResultRepository.steamCsvWithConsumerBySurveyId(surveyId, row -> {
                try {
                    List<String> headers = headersRef.get();
                    if (headers == null) {
                        // 初回のみヘッダーを決定して出力
                        headers = row.keySet().stream().toList();
                        printer.printRecord(headers);
                        headersRef.set(headers);
                    }
                    
                    // 決定したヘッダー順序に従って値を取得・出力する
                    List<String> currentHeaders = headers;
                    List<Object> values = currentHeaders.stream()
                            .map(row::get)
                            .toList();
                    printer.printRecord(values);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    @Override
    public SurveyAggregation findSurveyAggregationByIdAndAccount(Integer surveyId, UserAccount account)
            throws PermissionDeniedException {
        
        SurveyAggregation aggregation = surveyResultRepository.findBySurveyId(surveyId);

        if (aggregation == null) {
            throw new IllegalArgumentException("指定された集計結果が見つかりません: " + surveyId);
        }

        switch (aggregation.getSurvey().getResultVisibility()) {
            case ADMIN_ONLY -> {
                throw new PermissionDeniedException("この集計結果は管理者用機能からのみ閲覧可能です。");
            }
            case TARGET_GROUP -> {
                if (account.assignedGroups().stream().anyMatch(userGroup -> 
                    aggregation.getSurvey().getTargetGroups().contains(userGroup))) {
                    // User has permission
                } else {
                    throw new PermissionDeniedException("このアンケート結果を閲覧する権限がありません。");
                }
            }
            case ALL_USER -> {
                // User has permission
            }
        }

        return aggregation;
    }
}