package com.community.dailyrecordofbook.common.service;

import com.community.dailyrecordofbook.common.util.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Random;


@Service
public class MailSendService {

    private int size;

    @Autowired
    private JavaMailSenderImpl mailSender;

    // 인증키 생성
    private String getKey(int size) {
        this.size = size;
        return getAuthCode();
    }

    // 인증코드 난수 발생
    private String getAuthCode() {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        int num = 0;

        while (buffer.length() < size) {
            num = random.nextInt(10);
            buffer.append(num);
        }
        return buffer.toString();
    }

    public String sendAuthMail(String email) {
        // 6자리 난수 인증번호 생성
        String authKey = getKey(6);

        // 인증메일 보내기
        try {
            MailUtil sendMail = new MailUtil(mailSender);
            sendMail.setSubject("책방일지 회원가입 이메일 인증");
            sendMail.setText(new StringBuffer().append("<h1>[책방일지 회원가입 이메일 인증]</h1>")
                    .append("<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>")
                    .append("<a href='http://localhost:8080/user/joinConfirm?userEmail=").append(email)
                    .append("&authKey=").append(authKey).append("' target='_blank'>이메일 인증 확인</a>").toString());
            sendMail.setFrom("mnlst2020c@gmail.com", "관리자");
            sendMail.setTo(email);
            sendMail.send();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return authKey;
    }


    // 임시 비밀번호 보내는 메일
    public String sendTempPwMail(String email) {
        // 8자리 임시 비밀번호 생성
        String tempPw = getRamdomPassword(8);

        // 인증메일 보내기
        try {
            MailUtil sendMail = new MailUtil(mailSender);
            sendMail.setSubject("책방일지 회원 임시 비밀번호 발송 메일");
            sendMail.setText(new StringBuffer().append("<h1>[책방일지 회원 임시 비밀번호]</h1>")
                    .append("<p> 회원님의 임시 비밀번호는 ")
                    .append(tempPw)
                    .append(" 입니다.</p>").toString());
            sendMail.setFrom("mnlst2020c@gmail.com", "관리자");
            sendMail.setTo(email);
            sendMail.send();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return tempPw;
    }

    // 임시 비밀번호 생성
    public static String getRamdomPassword(int len) {
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        int idx = 0;
        StringBuffer sb = new StringBuffer();
        System.out.println("charSet.length :::: "+charSet.length);
        for (int i = 0; i < len; i++) {
            idx = (int) (charSet.length * Math.random()); // 36 * 생성된 난수를 Int로 추출 (소숫점제거)
            System.out.println("idx :::: "+idx);
            sb.append(charSet[idx]);
        }
        return sb.toString();
    }
}
