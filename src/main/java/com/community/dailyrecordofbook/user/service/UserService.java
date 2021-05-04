package com.community.dailyrecordofbook.user.service;

import com.community.dailyrecordofbook.common.service.MailSendService;
import com.community.dailyrecordofbook.user.dto.Join;
import com.community.dailyrecordofbook.user.entity.Role;
import com.community.dailyrecordofbook.user.entity.User;
import com.community.dailyrecordofbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSendService mailSendService;

    public void join(Join join) {

        if(userRepository.findByEmail(join.getEmail()).orElse(null) != null) {
            System.out.println("이미 가입된 회원입니다.");
        }

        String hashedPassword = passwordEncoder.encode(join.getPassword());
        join.setPassword(hashedPassword);
        join.setRole(Role.MAIL);
        join.setType("drob");

        try {
            User user = userRepository.save(new User(join));
            String authKey = mailSendService.sendAuthMail(join.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

}
