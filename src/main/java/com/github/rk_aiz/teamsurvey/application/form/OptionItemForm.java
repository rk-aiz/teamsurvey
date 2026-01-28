package com.github.rk_aiz.teamsurvey.application.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionItemForm {

    private Integer itemId;

    @Size(max = 255, message = "項目文は{max}文字以内で入力してください")
    @NotBlank(message = "項目文は必須です")
    private String itemText;

    @NotNull(message = "項目の順番を指定してください")
    private Integer itemOrder;
}