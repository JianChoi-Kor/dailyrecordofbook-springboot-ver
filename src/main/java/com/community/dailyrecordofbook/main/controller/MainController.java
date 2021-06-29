package com.community.dailyrecordofbook.main.controller;

import com.community.dailyrecordofbook.main.dto.AddBook;
import com.community.dailyrecordofbook.main.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final MainService mainService;

    @GetMapping(value = {"/", "/main"})
    public String main(Model model) {
        model.addAttribute("bookList", mainService.getBookList());
        return "main";
    }

    @GetMapping("/addBook")
    public String addBookView() {
        return "addBook";
    }

    @PostMapping("/addBookSlide")
    public String addBook(AddBook addBook, MultipartFile bookSlideFile, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mainService.addBook(addBook, bookSlideFile, request, response);
    }

    @ResponseBody
    @DeleteMapping("/delBook")
    public int delBook(@RequestParam Long bookIdx, @RequestParam Long loginUserIdx) throws Exception {

        return mainService.delBook(bookIdx, loginUserIdx);
    }
}