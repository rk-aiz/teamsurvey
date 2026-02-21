package com.github.rk_aiz.teamsurvey.exception;

public class LoginRequiredException extends RuntimeException {

    public LoginRequiredException(String message) {
        super(message);
    }
}