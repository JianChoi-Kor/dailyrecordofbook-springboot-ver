package com.community.dailyrecordofbook.user.service;

import com.community.dailyrecordofbook.common.config.auth.dto.SessionUser;
import com.community.dailyrecordofbook.common.service.MailSendService;
import com.community.dailyrecordofbook.user.dto.Join;
import com.community.dailyrecordofbook.user.dto.JoinConfirm;
import com.community.dailyrecordofbook.user.dto.Login;
import com.community.dailyrecordofbook.user.entity.Role;
import com.community.dailyrecordofbook.user.entity.User;
import com.community.dailyrecordofbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSendService mailSendService;
    private final AuthenticationManager authenticationManager;
    private final HttpSession httpSession;


    @Transactional
    public String join(Join join) {
        if(userRepository.findByEmail(join.getEmail()).orElse(null) != null) {
            System.out.println("이미 가입된 회원입니다.");
        }

        join.setPassword(passwordEncoder.encode(join.getPassword()));
        join.setRole(Role.MAIL);
        join.setType("drob");

        try {
            User user = new User(join);
            return sendAndSaveAuthKey(user);

        } catch (Exception e) {
            e.printStackTrace();
            return "user/joinFailure";
        }
    }

    private String sendAndSaveAuthKey(User user) {
        String authKey = mailSendService.getKey(6);
        mailSendService.sendAuthMail(user.getEmail(), authKey);
        user.setAuthKey(authKey);
        userRepository.save(user);
        return "user/joinSuccess";
    }

    public int emailChk(String email) {
        if(userRepository.findByEmail(email).orElse(null) == null) {
            // 가입할 수 있는 이메일입니다.
            return 0;
        } else {
            // 이미 가입된 이메일입니다.
            return 1;
        }
    }

    public String chkAndUpdAuth(JoinConfirm joinConfirm) {
        User user = userRepository.findByEmail(joinConfirm.getEmail()).orElse(null);

        System.out.println("User AuthKey :::" + user.getAuthKey());
        System.out.println("Mail AuthKey :::" + joinConfirm.getAuthKey());

        if(user.getAuthKey().equals(joinConfirm.getAuthKey())) {
            user.setRole(Role.USER);
            userRepository.save(user);
            return "user/mailSuccess";
        } else {
            return "user/mailFailure";
        }
    }

    public int login(Login login) {
        try {
            // 아이디와 패스워드를 Secutiry가 알아볼 수 있는 token 객체로 변환한다.
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword(), new ArrayList<>());
            // AuthenticationManager에 token을 넘기면 UserDetailsService가 받아 처리하도록 한다.
            Authentication authentication = authenticationManager.authenticate(token);
        } catch (Exception e) {
            e.printStackTrace();
            return 1; // 로그인 실패
        }
        User user = userRepository.findByEmail(login.getEmail()).orElse(null);

        if(user.getRole() == Role.MAIL) {
            return 2; // 이메일 인증 전
        }
        httpSession.setAttribute("user", new SessionUser(user));
        return 0;
    }

}
