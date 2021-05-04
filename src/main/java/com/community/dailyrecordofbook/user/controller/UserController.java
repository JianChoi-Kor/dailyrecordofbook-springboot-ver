package com.community.dailyrecordofbook.user.controller;



import com.community.dailyrecordofbook.user.dto.Join;
import com.community.dailyrecordofbook.user.entity.User;
import com.community.dailyrecordofbook.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


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

        userService.join(join);
        return "main";
    }


    @GetMapping("/subInfo")
    public String subInfo() {
        return "user/subInfo";
    }


    @ResponseBody
    @PostMapping("/emailChk")
    public int emailChk(@RequestBody User user) {
        return userService.emailChk(user.getEmail());
    }
}
