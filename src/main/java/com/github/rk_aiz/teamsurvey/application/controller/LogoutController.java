package com.github.rk_aiz.teamsurvey.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/logout")
@RequiredArgsConstructor
public class LogoutController {
    public String logout() {
        return "redirect:/logout";
    }
}