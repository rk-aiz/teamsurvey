package com.github.rk_aiz.teamsurvey.infrastructure.repository_impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;
import com.github.rk_aiz.teamsurvey.domain.repository.AnswerOptionRepository;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.AnswerPatternEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.entity.AnswerPatternItemEntity;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.AnswerPatternItemMapper;
import com.github.rk_aiz.teamsurvey.infrastructure.mapper.mybatis.AnswerPatternMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AnswerOptionRepositoryImpl implements AnswerOptionRepository {

    /**
     * AnswerOptionが集約ルート(Aggregate Root)として定義されているため、
     * このリポジトリはルートエンティティ(AnswerPattern)と子エンティティ(AnswerPatternItem)の両方の永続化を担当します。
     * そのため、内部的に2つのMapperを使用して集約単位での整合性を管理しています。
     */
    private final AnswerPatternMapper answerPatternMapper;
    private final AnswerPatternItemMapper answerPatternItemMapper;

    @Override
    public List<AnswerOption> findAll() {
        return this.answerPatternMapper.selectAll()
                .stream().map(AnswerPatternEntity::toModel).toList();
    }

    @Override
    public List<AnswerOption> selectAllWithItems() {
        List<AnswerOption> options = this.answerPatternMapper.selectAllWithItems()
                .stream().map(AnswerPatternEntity::toModel).toList();

        for (AnswerOption option : options) {
            this.answerPatternItemMapper
                    .selectByPatternId(option.getAnswerOptionId())
                    .forEach(item -> {
                        option.addItem(item.getId(), item.getItemText(), item.getItemOrder());
                    });
        }
        return options;
    }

    @Override
    public AnswerOption findById(Integer id) {
        AnswerPatternEntity entity = this.answerPatternMapper.selectById(id);
        if (entity == null) return null;
        
        AnswerOption answerOption = entity.toModel();
        this.answerPatternItemMapper
                .selectByPatternId(entity.getId())
                .forEach(item -> {
                    answerOption.addItem(item.getId(), item.getItemText(), item.getItemOrder());
                });
        
        return answerOption;
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
                AnswerPatternItemEntity itemEntity = new AnswerPatternItemEntity();
                itemEntity.setAnswerPatternId(entity.getId());
                itemEntity.setItemText(item.getItemText());
                itemEntity.setItemOrder(item.getItemOrder());
                this.answerPatternItemMapper.insert(itemEntity);
                item.setId(itemEntity.getId());
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
                if (item.getId() != null && currentMap.containsKey(item.getId())) {
                    // Update
                    AnswerPatternItemEntity existing = currentMap.get(item.getId());
                    existing.setItemText(item.getItemText());
                    existing.setItemOrder(item.getItemOrder());
                    this.answerPatternItemMapper.update(existing);
                    currentMap.remove(item.getId());
                } else {
                    // Insert
                    AnswerPatternItemEntity newItem = new AnswerPatternItemEntity();
                    newItem.setAnswerPatternId(patternId);
                    newItem.setItemText(item.getItemText());
                    newItem.setItemOrder(item.getItemOrder());
                    this.answerPatternItemMapper.insert(newItem);
                    item.setId(newItem.getId());
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
}
