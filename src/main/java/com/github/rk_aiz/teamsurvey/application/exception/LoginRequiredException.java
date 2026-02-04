package com.github.rk_aiz.teamsurvey.application.exception;

public class LoginRequiredException extends RuntimeException {

    public LoginRequiredException(String message) {
        super(message);
    }
}