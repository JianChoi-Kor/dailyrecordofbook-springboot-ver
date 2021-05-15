package com.community.dailyrecordofbook.user.controller;

import com.community.dailyrecordofbook.user.dto.*;
import com.community.dailyrecordofbook.user.entity.User;
import com.community.dailyrecordofbook.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/terms")
    public String terms() { return "user/terms"; }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/join")
    public String join() {
        return "user/join";
    }

    @PostMapping("/join")
    public String join(Join join) {
        return userService.join(join);
    }

    @GetMapping("/joinConfirm")
    public String joinConfirm(JoinConfirm joinConfirm) {
        return userService.chkAndUpdAuth(joinConfirm);
    }

    @GetMapping("/joinSuccess")
    public String joinSuccess() {
        return "user/joinSuccess";
    }

    @GetMapping("joinFailure")
    public String joinFailure() {
        return "user/joinFailure";
    }

    @GetMapping("mailSuccess")
    public String mailSuccess() { return "user/mailSuccess"; }

    @GetMapping("mailFailure")
    public String mailFailure() { return "user/mailFailure"; }


    @GetMapping("/addInfo")
    public String addInfo() {
        return "user/addInfo";
    }

    @ResponseBody
    @PostMapping("/addInfo")
    public int addInfo(@RequestBody AddInfo addInfo) throws Exception {
        return userService.addInfo(addInfo);
    }

    @ResponseBody
    @PostMapping("/emailChk")
    public int emaiRlChk(@RequestBody User user) {
        return userService.emailChk(user.getEmail());
    }

    @ResponseBody
    @PostMapping("/login")
    public int login(@RequestBody Login login) throws Exception { return userService.login(login); }


    @GetMapping("/myPage")
    public ModelAndView myPage(HttpServletResponse response, ModelAndView modelAndView) throws Exception {
        return userService.myPage(response, modelAndView);
    }

    @ResponseBody
    @PostMapping("/myPage")
    public int uploadProfileImage(MultipartFile profileImg, HttpServletRequest request) throws Exception {
        return userService.uploadProfileImage(profileImg, request);
    }

    @GetMapping("/changePassword")
    public String changePassword() { return "user/changePassword"; }

    @ResponseBody
    @PostMapping("/changePassword")
    public int changePassword(@RequestBody Password password) throws Exception {
        return userService.changePassword(password);
    }

    @GetMapping("/withDraw")
    public String withDraw() { return "user/withDraw"; }

    @ResponseBody
    @PostMapping("/withDraw")
    public int withDraw(@RequestBody Password password) throws Exception {
        return userService.withDraw(password);
    }

}
