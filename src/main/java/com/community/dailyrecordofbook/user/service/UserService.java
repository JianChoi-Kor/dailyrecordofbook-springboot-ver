package com.community.dailyrecordofbook.user.service;

import com.community.dailyrecordofbook.board.dto.ListBoard;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
    public String join(Join join, HttpServletResponse response) throws Exception {
        SessionUser sessionUser = (SessionUser) SessionUtil.getAttribute("user");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if(userRepository.findByEmail(join.getEmail()).orElse(null) != null) {
            out.println("<script> alert('이미 회훤가입된 이메일입니다.'); location.href='/user/login'; </script>");
            out.flush();
            return "user/login";
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
            return 1; // 로그인 실패
        }
        User user = userRepository.findByEmail(login.getEmail()).orElse(null);
        if(!user.getType().equals("drob")) {
            return 3; // 소셜 로그인 회원인 경우
        }

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
            out.println("<script> alert('올바르지 않은 접근입니다.'); location.href='/main'; </script>");
            out.flush();
            modelAndView.setViewName("main");
            return modelAndView;
        }

        if(sessionUser.getRole() == Role.TEMP) {
            out.println("<script> if(confirm('추가정보를 입력하지 않으셨습니다. 추가정보를 입력하시겠습니까?')) {location.href='/user/addInfo';} else {location.href='/main';} </script>");
            out.flush();
            modelAndView.setViewName("main");
            return modelAndView;
        }

        User user = userRepository.findByEmail(sessionUser.getEmail()).orElse(null);
        modelAndView.addObject("userInfo", user);

        if(user != null) {
            modelAndView.setViewName("user/myPage");
            return modelAndView;
        } else {
            out.println("<script> alert('올바르지 않은 접근입니다.'); location.href='/main';</script>");
            out.flush();
            modelAndView.setViewName("main");
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
//        String localPath = "/Users/jianchoi/dailyrecordofbook";

        String basePath = rootPath + "res/image/user/" + user.getIdx();
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

        String filePath = "/res/image/user/" + user.getIdx() + "/" + profileImg;
        
        user.update(user.getName(), filePath);
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
        List<User> userList = userCustomRepositorySupport.findEmail(findEmail);
        if(userList.size() > 1) {
            StringBuilder emailList = new StringBuilder();
            for(int i = 0; i < userList.size(); i++) {
                emailList.append(userList.get(i).getEmail());
                emailList.append(", ");
            }
            emailList.substring(0, emailList.length()-2);
        }

        FindEmailResponse response = new FindEmailResponse();
        if(ObjectUtils.isEmpty(userList)) {
            response.setResult("notFound");
        } else if(userList.size() > 0) {
            StringBuilder emailList = new StringBuilder();
            for(int i = 0; i < userList.size(); i++) {
                emailList.append(userList.get(i).getEmail());
                emailList.append(", ");
            }
            System.out.println(emailList.toString());
            String result = emailList.substring(0, emailList.length()-2);
            response.setResult(result);
        } else {
            response.setResult("잘못된 요청입니다.");
        }
        return response;
    }

    @Transactional
    public int findPassword(FindPassword findPassword) {
        User user = userCustomRepositorySupport.findPassword(findPassword);
        if(user != null) {
            return sendTempPassword(user);
        } else {
            return 1; // 해당하는 유저가 없는 경우
        }
    }

    private int sendTempPassword(User user) {
        String tempPassword = mailSendService.getRandomPassword(10);
        try {
            mailSendService.sendTempPassword(user.getEmail(), tempPassword);
            tempPassword = passwordEncoder.encode(tempPassword);
            userRepository.save(user.updatePassword(tempPassword));
            return 0;
        } catch (Exception e) {
            return 2; // 임시 비밀번호 발송 오류
        }
    }

    public ModelAndView getUserList(HttpServletResponse response, ModelAndView modelAndView, Integer page, String search) throws Exception {
        SessionUser sessionUser = (SessionUser) SessionUtil.getAttribute("user");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if(sessionUser == null || sessionUser.getRole() != Role.ADMIN) {
            out.println("<script> alert('올바르지 않은 접근입니다.'); location.href='/main'; </script>");
            out.flush();
            modelAndView.setViewName("/main");
            return modelAndView;
        }

        if(page == null || page <= 0) {
            page = 0;
        }
        Integer pageSize = 15;

        Pageable pageable = PageRequest.of(page, pageSize);
        PageImpl<ListUser> userList = userCustomRepositorySupport.getList(pageable, search);

        modelAndView.addObject("pagination", userList);
        modelAndView.setViewName("user/list");

        return modelAndView;
    }
}
