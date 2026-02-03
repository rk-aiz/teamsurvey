package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;
import com.github.rk_aiz.teamsurvey.domain.model.Response;
import com.github.rk_aiz.teamsurvey.domain.model.ResponseDetail;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.model.question.SingleChoiceQuestion;
import com.github.rk_aiz.teamsurvey.domain.model.result.SurveyAggregation;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyResultService;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.SurveyRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.SurveyResultRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyResultServiceImpl implements SurveyResultService {

    private final SurveyResultRepository surveyResultRepository;
    private final SurveyRepository surveyRepository;

    @Override
    public List<SurveyAggregation> findAllSurveyAggregations() {
        return surveyResultRepository.findAll();
    }

    @Override
    public SurveyAggregation findSurveyAggregationById(Integer surveyId) throws IllegalArgumentException {
        return surveyResultRepository.findBySurveyId(surveyId);
    }

    @Override
    public String generateCsv(Integer surveyId) {
        // 1. アンケート定義取得 (設問ヘッダー用)
        Survey survey = surveyRepository.findById(surveyId);
        if (survey == null) {
            throw new IllegalArgumentException("Survey not found: " + surveyId);
        }

        // 2. 回答データ取得
        List<Response> responses = surveyResultRepository.findResponsesForCsv(surveyId);

        // 3. 選択肢ID -> テキスト のマッピング作成 (高速化のため)
        Map<Integer, String> optionMap = new HashMap<>();
        for (Question q : survey.getQuestions()) {
            if (q instanceof SingleChoiceQuestion) {
                AnswerOption option = ((SingleChoiceQuestion) q).getAnswerOption();
                if (option != null && option.getItems() != null) {
                    for (AnswerOption.OptionItem item : option.getItems()) {
                        optionMap.put(item.getItemId(), item.getItemText());
                    }
                }
            }
        }

        // 4. CSV構築
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        // Header
        sb.append("回答ID,回答日時,ユーザー名");
        List<Question> questions = (List<Question>) survey.getQuestions();
        for (Question q : questions) {
            sb.append(",").append(escapeCsv(q.getText()));
        }
        sb.append("\r\n");

        // Body
        for (Response r : responses) {
            sb.append(r.getResponseId()).append(",");
            sb.append(r.getCreatedAt().format(dtf)).append(",");
            sb.append(escapeCsv(r.getUsername()));

            // 回答詳細をMap化 (QuestionId -> RawData)
            Map<Integer, String> answerMap = r.getResponseDetails().stream()
                    .collect(Collectors.toMap(
                            d -> d.getQuestion().getQuestionId(),
                            ResponseDetail::getRawData,
                            (v1, v2) -> v1 // 重複時は最初を採用
                    ));

            for (Question q : questions) {
                sb.append(",");
                String raw = answerMap.get(q.getQuestionId());
                if (raw != null) {
                    // 選択肢IDが含まれる場合(数値のみ、またはカンマ区切りの数値)はテキストに変換
                    // 自由記述でも数字のみの場合があるが、optionMapにヒットしなければそのまま出力されるのでOK
                    sb.append(escapeCsv(convertIdsToTexts(raw, optionMap)));
                }
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    private String convertIdsToTexts(String raw, Map<Integer, String> optionMap) {
        // カンマ区切りで分割して変換を試みる
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .map(val -> {
                    try {
                        Integer id = Integer.valueOf(val);
                        return optionMap.getOrDefault(id, val);
                    } catch (NumberFormatException e) {
                        return val;
                    }
                })
                .collect(Collectors.joining(", "));
    }

    private String escapeCsv(String text) {
        if (text == null)
            return "";
        if (text.contains(",") || text.contains("\"") || text.contains("\n") || text.contains("\r")) {
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }
        return text;
    }
}