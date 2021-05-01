package com.community.dailyrecordofbook.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final HttpSession httpSession;

    @GetMapping(value = {"/", "/main"})
    public String main(Model model) {
        return "main";
    }

}
