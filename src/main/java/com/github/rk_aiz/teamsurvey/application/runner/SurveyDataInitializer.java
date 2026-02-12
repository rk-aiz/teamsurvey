package com.github.rk_aiz.teamsurvey.application.runner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;
import com.github.rk_aiz.teamsurvey.domain.model.Response;
import com.github.rk_aiz.teamsurvey.domain.model.ResponseDetail;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.model.question.MultiChoiceQuestion;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.model.question.SingleChoiceQuestion;
import com.github.rk_aiz.teamsurvey.domain.service.AccountService;
import com.github.rk_aiz.teamsurvey.domain.service.ResponseService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyService;
import com.github.rk_aiz.teamsurvey.domain.type.QuestionType;
import com.github.rk_aiz.teamsurvey.domain.type.ResponseStatus;
import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SurveyDataInitializer implements CommandLineRunner {

    private final SurveyService surveyService;
    private final ResponseService responseService;
    private final AccountService accountService;

    @Value("${spring.sql.init.mode:embedded}")
    private String sqlInitMode;

    @Override
    public void run(String... args) throws Exception {
        // spring.sql.init.mode が never の場合は初期データ投入をスキップ
        if ("never".equalsIgnoreCase(sqlInitMode)) {
            log.info("spring.sql.init.mode is 'never'. Skipping Java data initialization.");
            return;
        }

        initializeTestData();
    }

    private void initializeTestData() {
        log.info("初期データのアンケートステータス更新を開始します...");

        // ID=1: ITエンジニア意識調査
        publishSurvey(1);

        // ID=2: サービス満足度調査
        publishSurvey(2);

        // ID=3: 【下書き】新規企画アンケート (DRAFTのまま)

        // ID=4: 社内イベント感想 (CLOSEDにするため、一度公開してから終了)
        if (publishSurvey(4)) {
            closeSurvey(4);
        }

        // ID=5: 新入社員研修の感想
        publishSurvey(5);

        // ID=6: キャリア・昇進に関するアンケート
        publishSurvey(6);

        // ID=7: 春のお花見イベント参加確認
        publishSurvey(7);

        // ID=8: 開発ツールと技術関心に関する調査
        publishSurvey(8);

        // 回答データの生成(スナップショット化されたパターンIDに追従して登録)
        createTestResponses();

        log.info("初期データのアンケートステータス更新が完了しました。");
    }

    private boolean publishSurvey(Integer id) {
        try {
            // 公開処理(ここでスナップショット化ロジックが走る)
            surveyService.tryChangeStatusById(id, SurveyStatus.PUBLISHED);
            log.info("Survey ID={} を公開しました。", id);
            return true;
        } catch (Exception e) {
            log.error("Survey ID={} の公開に失敗しました: {}", id, e.getMessage());
            return false;
        }
    }

    private void closeSurvey(Integer id) {
        try {
            surveyService.tryChangeStatusById(id, SurveyStatus.CLOSED);
            log.info("Survey ID={} を終了しました。", id);
        } catch (Exception e) {
            log.error("Survey ID={} の終了に失敗しました: {}", id, e.getMessage());
        }
    }

    /**
     * テスト用回答データを生成します。
     * 公開処理によって回答パターンIDが変わっていても、動的にIDを取得して紐付けます。
     */
    private void createTestResponses() {
        log.info("テスト用回答データの生成を開始します...");

        // 1. Survey ID=1 (ITエンジニア意識調査) by admin
        // Q1(Order 1): 簡単(Order 4), Q2(Order 2): いいえ(Order 2)
        createResponse(1, "admin", Map.of(1, 4, 2, 2));

        // user2: 参加、焼き鳥希望
        createResponse(7, "user2", Map.of(1, 1, 2, "焼き鳥が食べたいです！"));
        // user3: 不参加
        createResponse(7, "user3", Map.of(1, 2, 2, ""));
        // user4: 参加、アレルギーあり
        createResponse(7, "user4", Map.of(1, 1, 2, "甲殻類アレルギーがあります。エビ・カニは避けていただけると助かります。"));
        // user5: 参加、デザート希望
        createResponse(7, "user5", Map.of(1, 1, 2, "食後のデザートに甘いものが欲しいです。"));
        // user6: 不参加
        createResponse(7, "user6", Map.of(1, 2, 2, ""));
        // user7: 参加、ピザ希望
        createResponse(7, "user7", Map.of(1, 1, 2, "ピザとかジャンクなものが食べたいです。"));
        // user8: 参加、特になし
        createResponse(7, "user8", Map.of(1, 1, 2, "特にありません。楽しみにしてます！"));
        // user9: 不参加
        createResponse(7, "user9", Map.of(1, 2, 2, ""));
        // user10: 参加、寿司希望
        createResponse(7, "user10", Map.of(1, 1, 2, "お寿司があると嬉しいです。"));

        // 3. Survey ID=8 (開発ツール...) by user
        // Q1(Order 1): スマホ(1), ノートPC(2) -> List.of(1, 2)
        // Q2(Order 2): コスト(1)
        // Q3(Order 3): プログラミング(1)
        // Q4(Order 4): Web検索(1)
        createResponse(8, "user", Map.of(
                1, List.of(1, 2),
                2, List.of(1),
                3, List.of(1),
                4, List.of(1)));

        // Survey ID=5 (新入社員研修) - 複数ユーザー
        // user2: 満足度4(Order 2), 普通(Order 3), "同期と..."
        createResponse(5, "user2", Map.of(1, 2, 2, 3, 3, "同期と仲良くなれました"));
        // user3: 満足度5(Order 1), 簡単(Order 4), null
        createResponse(5, "user3", Map.of(1, 1, 2, 4, 3, ""));
        // user4: 満足度3(Order 3), 難しい(Order 2), "時間が..."
        createResponse(5, "user4", Map.of(1, 3, 2, 2, 3, "時間が足りなかった"));

        log.info("テスト用回答データの生成が完了しました。");
    }

    /**
     * 回答登録ヘルパー
     * 
     * @param surveyId アンケートID
     * @param username 回答者ユーザー名
     * @param answers  Key:設問の表示順(displayOrder), Value:回答内容(Integer=ItemOrder,
     *                 String=Text, List=MultiItemOrder)
     */
    @SuppressWarnings("unchecked")
    private void createResponse(Integer surveyId, String username, Map<Integer, Object> answers) {
        try {
            // 最新のアンケート情報を取得(スナップショット化された回答パターンを含む)
            Survey survey = surveyService.findSurveyById(surveyId);
            UserAccount user = accountService.findAccountByUsername(username);

            Response response = responseService.createNewResponseBySurvey(survey, user);
            response.setStatus(ResponseStatus.VALID);
            response.setCreatedAt(LocalDateTime.now());
            response.setUpdatedAt(LocalDateTime.now());

            List<ResponseDetail> details = new ArrayList<>();

            for (Map.Entry<Integer, Object> entry : answers.entrySet()) {
                int displayOrder = entry.getKey();
                Object val = entry.getValue();

                // 表示順で設問を特定
                Question question = survey.getQuestions().stream()
                        .filter(q -> q.getDisplayOrder() == displayOrder)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Question not found for order: " + displayOrder));

                ResponseDetail detail = new ResponseDetail();
                detail.setQuestion(question);

                if (question.getType() == QuestionType.TEXT) {
                    detail.setTextResponse((String) val);
                } else if (question.getType() == QuestionType.RADIO) {
                    Integer order = (Integer) val;
                    SingleChoiceQuestion scq = (SingleChoiceQuestion) question;
                    // ItemOrderで選択肢IDを特定
                    Integer itemId = scq.getAnswerOption().getItems().stream()
                            .filter(i -> i.getItemOrder().equals(order))
                            .findFirst()
                            .map(i -> i.getItemId())
                            .orElseThrow(() -> new IllegalStateException("Item not found for order: " + order));
                    detail.setSingleChoiceResponse(itemId);
                } else if (question.getType() == QuestionType.CHECKBOX) {
                    List<Integer> orders = (List<Integer>) val;
                    MultiChoiceQuestion mcq = (MultiChoiceQuestion) question;
                    List<Integer> itemIds = mcq.getAnswerOption().getItems().stream()
                            .filter(i -> orders.contains(i.getItemOrder()))
                            .map(i -> i.getItemId())
                            .toList();
                    detail.setMultiChoiceResponses(itemIds);
                }
                details.add(detail);
            }

            response.setResponseDetails(details);
            responseService.saveResponse(survey, response);

        } catch (Exception e) {
            log.error("Failed to create response for survey {} user {}: {}", surveyId, username, e.getMessage());
        }
    }
}
