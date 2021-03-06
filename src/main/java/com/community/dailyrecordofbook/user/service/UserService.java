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
            out.println("<script> alert('?????? ??????????????? ??????????????????.'); location.href='/user/login'; </script>");
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
            // ????????? ??? ?????? ??????????????????.
            return 0;
        } else {
            // ?????? ????????? ??????????????????.
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
            // ???????????? ??????????????? Secutiry??? ????????? ??? ?????? token ????????? ????????????.
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword(), new ArrayList<>());
            // AuthenticationManager??? token??? ????????? UserDetailsService??? ?????? ??????????????? ??????.
            Authentication authentication = authenticationManager.authenticate(token);
        } catch (Exception e) {
            return 1; // ????????? ??????
        }
        User user = userRepository.findByEmail(login.getEmail()).orElse(null);
        if(!user.getType().equals("drob")) {
            return 3; // ?????? ????????? ????????? ??????
        }

        if(user.getRole() == Role.MAIL) {
            return 2; // ????????? ?????? ???
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
            return 1; // ?????? ?????? ?????? ??????
        }
        return 0;
    }


    public ModelAndView myPage(HttpServletResponse response, ModelAndView modelAndView) throws Exception {
        SessionUser sessionUser = (SessionUser) SessionUtil.getAttribute("user");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if(sessionUser == null) {
            out.println("<script> alert('???????????? ?????? ???????????????.'); location.href='/main'; </script>");
            out.flush();
            modelAndView.setViewName("main");
            return modelAndView;
        }

        if(sessionUser.getRole() == Role.TEMP) {
            out.println("<script> if(confirm('??????????????? ???????????? ??????????????????. ??????????????? ?????????????????????????')) {location.href='/user/addInfo';} else {location.href='/main';} </script>");
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
            out.println("<script> alert('???????????? ?????? ???????????????.'); location.href='/main';</script>");
            out.flush();
            modelAndView.setViewName("main");
        }
        return modelAndView;
    }

    public int uploadProfileImage(MultipartFile multipartFile, HttpServletRequest request) throws Exception {
        SessionUser sessionUser = (SessionUser) SessionUtil.getAttribute("user");
        if(sessionUser == null) {
            return 1; // ????????? ??????
        }
        if(multipartFile == null) {
            return 2; // ????????? ?????? ??????
        }
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElse(null);

        String rootPath = request.getSession().getServletContext().getRealPath("/");
//        String localPath = "/Users/jianchoi/dailyrecordofbook";

        String basePath = rootPath + "res/image/user/" + user.getIdx();
        String profileImg = fileUtil.transferTo(multipartFile, basePath);

        if(profileImg == null) {
            return 3; // ?????? ?????? ??????
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
            return 1; // ???????????? ?????????
        } else if(!password.getNewPassword().equals(password.getNewPasswordRe())) {
            return 2; // ????????? ???????????? ?????????
        } else if(password.getOriginPassword().equals(password.getNewPassword())) {
            return 3; // ?????? ??????????????? ????????? ????????????
        }

        String newPassword = passwordEncoder.encode(password.getNewPassword());
        userRepository.save(user.updatePassword(newPassword));
        return 0;
    }

    public int withDraw(Password password) throws Exception {
        SessionUser sessionUser = (SessionUser) SessionUtil.getAttribute("user");
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElse(null);

        if(!passwordEncoder.matches(password.getOriginPassword(), user.getPassword())) {
            return 1; // ???????????? ?????????
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
            response.setResult("????????? ???????????????.");
        }
        return response;
    }

    @Transactional
    public int findPassword(FindPassword findPassword) {
        User user = userCustomRepositorySupport.findPassword(findPassword);
        if(user != null) {
            return sendTempPassword(user);
        } else {
            return 1; // ???????????? ????????? ?????? ??????
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
            return 2; // ?????? ???????????? ?????? ??????
        }
    }

    public ModelAndView getUserList(HttpServletResponse response, ModelAndView modelAndView, Integer page, String search) throws Exception {
        SessionUser sessionUser = (SessionUser) SessionUtil.getAttribute("user");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if(sessionUser == null || sessionUser.getRole() != Role.ADMIN) {
            out.println("<script> alert('???????????? ?????? ???????????????.'); location.href='/main'; </script>");
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
