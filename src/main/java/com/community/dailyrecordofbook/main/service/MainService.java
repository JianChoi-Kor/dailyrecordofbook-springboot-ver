package com.community.dailyrecordofbook.main.service;

import com.community.dailyrecordofbook.common.config.auth.dto.SessionUser;
import com.community.dailyrecordofbook.common.util.FileUtil;
import com.community.dailyrecordofbook.common.util.SessionUtil;
import com.community.dailyrecordofbook.main.dto.AddBook;
import com.community.dailyrecordofbook.main.entity.BookSlide;
import com.community.dailyrecordofbook.main.repository.BookSlideRepository;
import com.community.dailyrecordofbook.user.entity.Role;
import com.community.dailyrecordofbook.user.entity.User;
import com.community.dailyrecordofbook.user.repository.UserCustomRepositorySupport;
import com.community.dailyrecordofbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MainService {

    private final BookSlideRepository bookSlideRepository;
    private final UserCustomRepositorySupport userCustomRepositorySupport;
    private final FileUtil fileUtil;

    @Transactional
    public String addBook(AddBook addBook, MultipartFile bookSlideFile, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionUser sessionUser = (SessionUser) SessionUtil.getAttribute("user");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if(sessionUser == null) {
            out.println("<script> alert('올바르지 않은 접근입니다.'); history.back();</script>");
            out.flush();
            return "main";
        }

        if(sessionUser.getRole() != Role.ADMIN) {
            out.println("<script> alert('올바르지 않은 접근입니다.'); history.back();</script>");
            out.flush();
            return "main";
        }

        BookSlide bookSlide = new BookSlide(addBook);

        String rootPath = request.getSession().getServletContext().getRealPath("/");
        String date = fileUtil.getDate();

        String basePath = rootPath + "res/image/book/" + date;
        String bookImage = fileUtil.transferTo(bookSlideFile, basePath);

        if(bookImage == null) {
            out.println("<script> alert('이미 업로드에 실패했습니다.'); history.back();</script>");
            out.flush();
            return "main";
        }

        String filePath = "/res/image/book/" + date + "/" + bookImage;
        bookSlide.setBookImg(filePath);

        bookSlideRepository.save(bookSlide);
        response.sendRedirect("main");
        return "main";
    }

    public List<BookSlide> getBookList() {
        return bookSlideRepository.findAll();
    }

    public int delBook(Long bookIdx, Long loginUserIdx) throws Exception {
        User user = userCustomRepositorySupport.findByIdx(loginUserIdx);
        if(ObjectUtils.isEmpty(user)) {
            return 1;
        }
        if(user.getRole() != Role.ADMIN) {
            return 1;
        }

        BookSlide bookSlide = bookSlideRepository.findByIdx(bookIdx).orElse(null);
        if(bookSlide == null) {
            return 1;
        }
        bookSlideRepository.delete(bookSlide);
        return 0;
    }
}
