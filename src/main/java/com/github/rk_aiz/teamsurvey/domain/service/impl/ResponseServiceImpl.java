package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;
import com.github.rk_aiz.teamsurvey.domain.model.Response;
import com.github.rk_aiz.teamsurvey.domain.model.Response.ResponseBuilder;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.service.ResponseService;
import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.ResponseRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {

    private final ResponseRepository responseRepository;

    @Override
    public List<Response> findAllResponses() {
        return responseRepository.findAll();
    }

    @Override
    public Response findResponseById(Integer responseId) {
        return this.responseRepository.findById(responseId);
    }

    @Override
    public boolean saveResponse(Survey survey, Response response) {
        // DB上のステータスがPUBLISHED以外の場合、回答登録を許可しない
        if (survey.getStatus() != SurveyStatus.PUBLISHED)
            return false;

        if (response.getResponseId() == null) {
            // 新規登録
            responseRepository.add(response);
        } else {
            // 更新処理
            responseRepository.set(response);
        }

        return true;
    }

    @Override
    public Response createNewResponseBySurvey(Survey survey, UserAccount account) {

        ResponseBuilder responseBuilder = Response.builder()
                .surveyId(survey.getId())
                .username(account.username());

        return responseBuilder.build();
    }

    @Override
    public List<Response> findResponseBySurveyId(Integer surveyId) {
        return this.responseRepository.findBySurveyId(surveyId);
    }

    @Override
    public List<Response> findResponseByUsername(String username) {
        return responseRepository.findByUsername(username);
    }
}
