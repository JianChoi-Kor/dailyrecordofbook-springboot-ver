package com.community.dailyrecordofbook.main.controller;

import com.community.dailyrecordofbook.main.dto.AddBook;
import com.community.dailyrecordofbook.main.entity.MainBanner;
import com.community.dailyrecordofbook.main.service.MainService;
import javassist.NotFoundException;
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
        model.addAttribute("bannerList", mainService.getBannerList());
        model.addAttribute("communityList", mainService.getCommunityList());
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
    public int delBook(@RequestParam Long bookIdx, @RequestParam Long loginUserIdx, HttpServletRequest request) throws Exception {
        return mainService.delBook(bookIdx, loginUserIdx, request);
    }

    @GetMapping("/addBanner")
    public String addBanner() {
        return "addBanner";
    }

    @ResponseBody
    @DeleteMapping("/delBanner")
    public int delBanner(@RequestParam Long bannerIdx, @RequestParam Long loginUserIdx, HttpServletRequest request) throws Exception {
        return mainService.delBanner(bannerIdx, loginUserIdx, request);
    }

    @PostMapping("/addMainBanner")
    public String addBanner(MainBanner mainBanner, MultipartFile bannerFile, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mainService.addMainBanner(mainBanner, bannerFile, request, response);
    }

    @GetMapping("/where")
    public String where() {
        return "where";
    }
}