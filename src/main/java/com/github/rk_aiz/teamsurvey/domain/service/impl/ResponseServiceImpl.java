package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.Response;
import com.github.rk_aiz.teamsurvey.domain.model.Response.ResponseBuilder;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.service.ResponseService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyService;
import com.github.rk_aiz.teamsurvey.domain.type.SurveyStatus;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.ResponseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {

    private final SurveyService surveyService;
    private final ResponseRepository responseRepository;

    @Override
    public List<Response> findAllResponses() {
        return responseRepository.findAll();
    }

    @Override
    public Response findResponseById(Integer responseId) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findResponseById'");
    }

    @Override
    public boolean saveResponse(Survey survey, Response response) {
        // DB上のステータスがPUBLISHED以外の場合、回答登録を許可しない
        if (survey.getStatus() != SurveyStatus.PUBLISHED) return false;

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
    public Response createNewResponseBySurvey(Survey survey, LoginUser loginUser) {

        ResponseBuilder responseBuilder = Response.builder()
                .surveyId(survey.getSurveyId())
                .username(loginUser.getUsername());

        return responseBuilder.build();
    }

    @Override
    public List<Response> findResponseBySurveyId(Integer surveyId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findResponseBySurveyId'");
    }
}
