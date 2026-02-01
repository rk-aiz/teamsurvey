package com.github.rk_aiz.teamsurvey.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.domain.model.Response;
import com.github.rk_aiz.teamsurvey.domain.model.Response.ResponseBuilder;
import com.github.rk_aiz.teamsurvey.domain.model.Survey;
import com.github.rk_aiz.teamsurvey.domain.service.ResponseService;
import com.github.rk_aiz.teamsurvey.domain.service.SurveyService;
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
    public boolean saveResponse(Response survey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveResponse'");
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
