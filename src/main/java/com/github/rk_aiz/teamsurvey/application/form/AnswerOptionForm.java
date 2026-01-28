package com.github.rk_aiz.teamsurvey.application.form;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;
import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption.OptionItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerOptionForm {

    private Integer answerOptionId;

    @Size(max = 200, message = "パターン名は{max}文字以内で入力してください")
    @NotBlank(message = "パターン名は必須です")
    private String name;

    // 1つのパターンは複数の選択肢項目を持つ
    @Builder.Default
    private List<OptionItemForm> items = new ArrayList<>();

    public AnswerOption toModel() {
        AnswerOption answerOption = new AnswerOption();
        BeanUtils.copyProperties(this, answerOption, "items");
        answerOption.setItems(this.getItems().stream().map(itemForm -> {
            OptionItem optionItem = new OptionItem();
            BeanUtils.copyProperties(itemForm, optionItem);
            return optionItem;
        }).toList());
        

        return answerOption;
    }

    public AnswerOptionForm from(AnswerOption answerOption) {
        AnswerOptionForm form = new AnswerOptionForm();
        BeanUtils.copyProperties(answerOption, form, "items");
        form.setItems(answerOption.getItems().stream().map(item -> {
            OptionItemForm itemForm = new OptionItemForm();
            BeanUtils.copyProperties(item, itemForm);
            return itemForm;
        }).toList());

        return form;
    }
}