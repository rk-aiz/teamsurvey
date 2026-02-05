package com.github.rk_aiz.teamsurvey.infrastructure.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;
import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption.OptionItem;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.AnswerPatternEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.AnswerPatternItemEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.AnswerPatternItemMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.AnswerPatternMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.AnswerOptionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AnswerOptionRepositoryImpl implements AnswerOptionRepository {

    /**
     * AnswerOptionが集約ルートとして定義されているため、
     * このリポジトリはルートエンティティ(AnswerPattern)と子エンティティ(AnswerPatternItem)の両方の永続化を担当します。
     */
    private final AnswerPatternMapper answerPatternMapper;
    private final AnswerPatternItemMapper answerPatternItemMapper;

    @Override
    public List<AnswerOption> findAll() {
        return this.answerPatternMapper.selectAll()
                .stream().map(AnswerPatternEntity::toModel).toList();
    }

    @Override
    public AnswerOption findById(Integer id) {
        return this.answerPatternMapper.selectById(id).toModel();
    }

    @Override
    public void add(AnswerOption answerOption) {
        AnswerPatternEntity entity = AnswerPatternEntity.from(answerOption);
        this.answerPatternMapper.insert(entity);
        // 自動採番IDをドメインモデルに反映
        answerOption.setAnswerOptionId(entity.getId());

        // 子要素(Items)の保存
        if (answerOption.getItems() != null) {
            for (AnswerOption.OptionItem item : answerOption.getItems()) {
                item.setItemId(insertItem(entity.getId(), item));
            }
        }
    }

    @Override
    public void set(AnswerOption answerOption) {
        this.answerPatternMapper.update(AnswerPatternEntity.from(answerOption));

        // 子要素(Items)の更新 (Smart Update)
        Integer patternId = answerOption.getAnswerOptionId();
        Map<Integer, AnswerPatternItemEntity> currentMap = this.answerPatternItemMapper.selectByPatternId(patternId)
                .stream().collect(Collectors.toMap(AnswerPatternItemEntity::getId, Function.identity()));

        List<AnswerOption.OptionItem> newItems = answerOption.getItems();
        if (newItems != null) {
            for (AnswerOption.OptionItem item : newItems) {
                if (item.getItemId() != null && currentMap.containsKey(item.getItemId())) {
                    // Update
                    AnswerPatternItemEntity existing = currentMap.get(item.getItemId());
                    existing.setItemText(item.getItemText());
                    existing.setItemOrder(item.getItemOrder());
                    this.answerPatternItemMapper.update(existing);
                    currentMap.remove(item.getItemId());
                } else {
                    // Insert
                    item.setItemId(insertItem(patternId, item));
                }
            }
        }

        // Delete removed items
        for (AnswerPatternItemEntity remaining : currentMap.values()) {
            this.answerPatternItemMapper.delete(remaining.getId());
        }
    }

    @Override
    public void remove(Integer id) {
        this.answerPatternItemMapper.deleteByPatternId(id);
        this.answerPatternMapper.delete(id);
    }

    private Integer insertItem(Integer patternId, OptionItem item) {
        AnswerPatternItemEntity newItem = new AnswerPatternItemEntity();
        BeanUtils.copyProperties(item, newItem);
        newItem.setAnswerPatternId(patternId);
        this.answerPatternItemMapper.insert(newItem);
        // 自動採番IDをreturn
        return newItem.getId();
    }
}
