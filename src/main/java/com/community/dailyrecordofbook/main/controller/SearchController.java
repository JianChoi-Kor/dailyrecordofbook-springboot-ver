package com.community.dailyrecordofbook.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;

@RequiredArgsConstructor
@RestController
public class SearchController {

    // dailyrecordofbook.com/robots.txt
    @GetMapping("/robots.txt")
    public String robots() {
        return "User-agent: Yeti\nAllow: /\n";
    }
}
