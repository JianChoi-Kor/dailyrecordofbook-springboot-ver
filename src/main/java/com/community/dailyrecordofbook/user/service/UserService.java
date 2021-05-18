package com.community.dailyrecordofbook.user.service;

import com.community.dailyrecordofbook.common.config.auth.dto.SessionUser;
import com.community.dailyrecordofbook.common.service.MailSendService;
import com.community.dailyrecordofbook.common.util.FileUtil;
import com.community.dailyrecordofbook.common.util.SessionUtil;
import com.community.dailyrecordofbook.user.dto.*;
import com.community.dailyrecordofbook.user.entity.Role;
import com.community.dailyrecordofbook.user.entity.User;
import com.community.dailyrecordofbook.user.repository.UserCustomRepositorySupport;
import com.community.dailyrecordofbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSendService mailSendService;
    private final AuthenticationManager authenticationManager;
    private final FileUtil fileUtil;
    private final UserCustomRepositorySupport userCustomRepositorySupport;

    @Transactional
    public String join(Join join) {
        if(userRepository.findByEmail(join.getEmail()).orElse(null) != null) {
            // TODO 수정 필요
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

        if(user.getAuthKey().equals(joinConfirm.getAuthKey())) {
            user.setRole(Role.USER);
            userRepository.save(user);
            return "user/mailSuccess";
        } else {
            return "user/mailFailure";
        }
    }

    public int login(Login login) throws Exception {

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
        SessionUtil.setAttribute("user", new SessionUser(user));
        return 0;
    }

    public int addInfo(AddInfo addInfo) throws Exception {
        SessionUser sessionUser = (SessionUser) SessionUtil.getAttribute("user");
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElse(null);

        try {
            user.addInfo(addInfo);
            user.setRole(Role.USER);
            userRepository.save(user);

            SessionUtil.setAttribute("user", new SessionUser(user));
        } catch (Exception e) {
            e.printStackTrace();
            return 1; // 추가 정보 입력 오류
        }
        return 0;
    }


    public ModelAndView myPage(HttpServletResponse response, ModelAndView modelAndView) throws Exception {
        SessionUser sessionUser = (SessionUser) SessionUtil.getAttribute("user");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if(sessionUser == null) {
            out.println("<script> alert('올바르지 않은 접근입니다.'); history.back();</script>");
            out.flush();
            modelAndView.setViewName("/main");
            return modelAndView;
        }

        if(sessionUser.getRole() == Role.TEMP) {
            out.println("<script> if(confirm('추가정보를 입력하지 않으셨습니다. 추가정보를 입력하시겠습니까?')) {location.href='/user/addInfo';} else {history.back();} </script>");
            out.flush();
            modelAndView.setViewName("/main");
            return modelAndView;
        }

        User user = userRepository.findByEmail(sessionUser.getEmail()).orElse(null);
        modelAndView.addObject("userInfo", user);

        if(user != null) {
            modelAndView.setViewName("user/myPage");
            return modelAndView;
        } else {
            out.println("<script> alert('올바르지 않은 접근입니다.'); history.back();</script>");
            out.flush();
            modelAndView.setViewName("/main");
        }
        return modelAndView;
    }

    public int uploadProfileImage(MultipartFile multipartFile, HttpServletRequest request) throws Exception {
        SessionUser sessionUser = (SessionUser) SessionUtil.getAttribute("user");
        if(sessionUser == null) {
            return 1; // 잘못된 경로
        }
        if(multipartFile == null) {
            return 2; // 파일이 없는 경우
        }
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElse(null);

        String rootPath = request.getSession().getServletContext().getRealPath("/");

        String basePath = rootPath + "/res/image/user/" + user.getIdx();
        String profileImg = fileUtil.transferTo(multipartFile, basePath);

        if(profileImg == null) {
            return 3; // 파일 생성 실패
        }

        if(user.getPicture() != null) {
            File file = new File(basePath, user.getPicture());
            if(file.exists()) {
                file.delete();
            }
        }
        
        user.update(user.getName(), profileImg);
        userRepository.save(user);
        SessionUtil.setAttribute("user", new SessionUser(user));
        return 0;
    }


    public int changePassword(Password password) throws Exception {
        SessionUser sessionUser = (SessionUser) SessionUtil.getAttribute("user");
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElse(null);

        if(!passwordEncoder.matches(password.getOriginPassword(), user.getPassword())) {
            return 1; // 비밀번호 불일치
        } else if(!password.getNewPassword().equals(password.getNewPasswordRe())) {
            return 2; // 새로운 비밀번호 불일치
        } else if(password.getOriginPassword().equals(password.getNewPassword())) {
            return 3; // 기존 비밀번호와 동일한 비밀번호
        }

        String newPassword = passwordEncoder.encode(password.getNewPassword());
        userRepository.save(user.updatePassword(newPassword));
        return 0;
    }

    public int withDraw(Password password) throws Exception {
        SessionUser sessionUser = (SessionUser) SessionUtil.getAttribute("user");
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElse(null);

        if(!passwordEncoder.matches(password.getOriginPassword(), user.getPassword())) {
            return 1; // 비밀번호 불일치
        } else {
            userRepository.delete(user);
            return 0;
        }
    }


    public FindEmailResponse findEmail(FindEmail findEmail) {
        User user = userCustomRepositorySupport.findEmail(findEmail);
        FindEmailResponse response = new FindEmailResponse();
        if(user == null) {
            response.setResult("notFound");
        } else {
            response.setResult(user.getEmail());
        }
        return response;
    }
}
