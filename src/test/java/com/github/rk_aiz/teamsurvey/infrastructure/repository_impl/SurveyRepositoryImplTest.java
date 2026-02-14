package com.github.rk_aiz.teamsurvey.infrastructure.repository_impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.model.UserGroup;
import com.github.rk_aiz.teamsurvey.domain.model.question.Question;
import com.github.rk_aiz.teamsurvey.domain.model.question.SingleChoiceQuestion;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.SurveyEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.SurveyMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.QuestionRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.UserGroupRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.impl.SurveyRepositoryImpl;

class SurveyRepositoryImplTest {

    private SurveyRepositoryImpl surveyRepository;
    private SurveyMapperStub surveyMapperStub;
    private QuestionRepositoryStub questionRepositoryStub;
    private UserGroupRepositoryStub userGroupRepositoryStub;

    @BeforeEach
    void setUp() {
        // スタブのインスタンス化
        surveyMapperStub = new SurveyMapperStub();
        questionRepositoryStub = new QuestionRepositoryStub();
        userGroupRepositoryStub = new UserGroupRepositoryStub();

        // テスト対象にスタブを注入
        surveyRepository = new SurveyRepositoryImpl(surveyMapperStub, questionRepositoryStub, userGroupRepositoryStub);
    }

    @Test
    void findAll_ReturnsListOfSurveys() {
        // Setup
        SurveyEntity entity = SurveyEntity.builder()
                .id(1)
                .title("Test Survey")
                .createdAt(LocalDateTime.now())
                .build();
        surveyMapperStub.insert(entity);

        // Execute
        List<Survey> result = surveyRepository.findAll();

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void findById_WhenExists_ReturnsSurveyWithQuestions() {
        Integer id = 1;
        // 1. Header Setup
        SurveyEntity entity = SurveyEntity.builder()
                .id(id)
                .title("Test Survey")
                .build();
        surveyMapperStub.insert(entity);

        // 2. Questions Setup
        Question question = new SingleChoiceQuestion();
        question.setId(10);
        question.setSurveyId(id);
        questionRepositoryStub.add(question);

        // 3. Groups Setup
        UserGroup userGroup = new UserGroup();
        userGroup.setId(20);
        userGroupRepositoryStub.addMapping(id, userGroup);

        // Execute
        Survey result = surveyRepository.findById(id);

        // Verify
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(1, result.getQuestions().size());
        assertEquals(10, result.getQuestions().iterator().next().getId());
        assertEquals(20, result.getTargetGroups().iterator().next().getId());
    }

    @Test
    void findById_WhenNotExists_ReturnsNull() {
        Survey result = surveyRepository.findById(99);
        assertNull(result);
    }

    @Test
    void add_SavesSurveyAndQuestions() {
        Survey newSurvey = Survey.builder()
                .title("New Survey")
                .questions(new ArrayList<>())
                .build();

        Question newQuestion = new SingleChoiceQuestion(); // ID is null
        newSurvey.getQuestions().add(newQuestion);

        // Execute
        boolean result = surveyRepository.add(newSurvey);

        // Verify
        assertTrue(result);
        assertNotNull(newSurvey.getId()); // IDが自動採番されていること

        // DB(Stub)の状態検証
        SurveyEntity savedEntity = surveyMapperStub.selectById(newSurvey.getId());
        assertNotNull(savedEntity);
        assertEquals("New Survey", savedEntity.getTitle());

        List<Question> savedQuestions = questionRepositoryStub.findBySurveyId(newSurvey.getId());
        assertEquals(1, savedQuestions.size());
        assertEquals(newSurvey.getId(), savedQuestions.get(0).getSurveyId());
    }

    @Test
    void updateHeader_UpdatesSurveyOnly() {
        // Setup existing
        SurveyEntity entity = SurveyEntity.builder()
                .id(1)
                .title("Old Title")
                .build();
        surveyMapperStub.insert(entity);

        // Update model
        Survey updateSurvey = Survey.builder()
                .id(1)
                .title("New Title")
                .build();

        // Execute
        boolean result = surveyRepository.updateHeader(updateSurvey);

        // Verify
        assertTrue(result);
        SurveyEntity updatedEntity = surveyMapperStub.selectById(1);
        assertEquals("New Title", updatedEntity.getTitle());
        
        // updateHeaderは質問の更新を行わないため、質問リポジトリへの操作検証は不要
    }

    @Test
    void remove_DeletesSurvey() {
        SurveyEntity entity = SurveyEntity.builder().id(1).build();
        surveyMapperStub.insert(entity);

        boolean result = surveyRepository.remove(1);

        assertTrue(result);
        assertNull(surveyMapperStub.selectById(1));
    }

    // --- Stub Classes (Fakes) ---
    // 実際のDBの代わりにメモリ上のMapやListでデータを管理する簡易実装
    static class SurveyMapperStub implements SurveyMapper {
        private final Map<Integer, SurveyEntity> db = new HashMap<>();
        private int idCounter = 1;

        @Override
        public List<SurveyEntity> selectAll() {
            return new ArrayList<>(db.values());
        }

        @Override
        public SurveyEntity selectById(Integer id) {
            return db.get(id);
        }

        @Override
        public List<SurveyEntity> selectByUsername(String username) {
            return new ArrayList<>(); // 必要に応じて実装
        }

        @Override
        public boolean existsMappingByIdAndUsername(Integer surveyId, String username) {
            return false; // 必要に応じて実装
        }

        @Override
        public int insert(SurveyEntity entity) {
            if (entity.getId() == null) {
                entity.setId(idCounter++);
            }
            db.put(entity.getId(), entity);
            return 1;
        }

        @Override
        public int update(SurveyEntity entity) {
            if (db.containsKey(entity.getId())) {
                db.put(entity.getId(), entity);
                return 1;
            }
            return 0;
        }

        @Override
        public int delete(Integer id) {
            return db.remove(id) != null ? 1 : 0;
        }
    }

    static class QuestionRepositoryStub implements QuestionRepository {
        private final List<Question> db = new ArrayList<>();
        private int idCounter = 1;

        @Override
        public List<Question> findBySurveyId(Integer surveyId) {
            return db.stream()
                    .filter(q -> Objects.equals(q.getSurveyId(), surveyId))
                    .toList();
        }

        @Override
        public boolean add(Question question) {
            if (question.getId() == null) {
                question.setId(idCounter++);
            }
            db.add(question);
            return true;
        }

        @Override
        public boolean set(Question question) {
            db.removeIf(q -> Objects.equals(q.getId(), question.getId()));
            db.add(question);
            return true;
        }

        // 未使用メソッドのダミー実装
        @Override public List<Question> findAll() { return List.of(); }
        @Override public Question findById(Integer id) { return null; }
        @Override public boolean remove(Integer id) { return false; }
        @Override public boolean removeBySurveyId(Integer surveyId) { return false; }
    }

    static class UserGroupRepositoryStub implements UserGroupRepository {
        private final Map<Integer, List<UserGroup>> surveyGroups = new HashMap<>();

        // テスト用ヘルパーメソッド
        public void addMapping(Integer surveyId, UserGroup group) {
            surveyGroups.computeIfAbsent(surveyId, k -> new ArrayList<>()).add(group);
        }

        @Override
        public List<UserGroup> findBySurveyId(Integer surveyId) {
            return surveyGroups.getOrDefault(surveyId, new ArrayList<>());
        }

        // 未使用メソッドのダミー実装
        @Override public List<UserGroup> findAll() { return List.of(); }
        @Override public UserGroup findById(Integer groupId) { return null; }
        @Override public List<UserGroup> findByUsername(String username) { return List.of(); }
        @Override public boolean existsByGroupName(String groupName) { return false; }
        @Override public void save(UserGroup userGroup) {}
        @Override public boolean remove(Integer groupId) { return false; }
        @Override public void updateUserGroupMapping(String username, List<Integer> groupIds) {}
    }
}