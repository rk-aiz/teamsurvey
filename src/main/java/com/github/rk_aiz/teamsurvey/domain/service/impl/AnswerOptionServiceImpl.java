package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.model.AnswerOption;
import com.github.rk_aiz.teamsurvey.domain.repository.AnswerOptionRepository;
import com.github.rk_aiz.teamsurvey.domain.service.AnswerOptionService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerOptionServiceImpl implements AnswerOptionService {

    private final AnswerOptionRepository answerOptionRepository;

    @Override
    public List<AnswerOption> findAll() {
        return answerOptionRepository.findAll();
    }

    @Override
    public AnswerOption findAnswerOptionById(Integer id) {
        return answerOptionRepository.findById(id);
    }

    @Override
    public void save(AnswerOption answerOption) {
        if (answerOption.getAnswerOptionId() == null) {
            answerOptionRepository.add(answerOption);
        } else {
            answerOptionRepository.set(answerOption);
        }
    }

    @Override
    public void remove(Integer id) {
        answerOptionRepository.remove(id);
    }

}
