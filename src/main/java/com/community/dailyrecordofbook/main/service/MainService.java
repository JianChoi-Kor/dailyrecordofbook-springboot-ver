package com.community.dailyrecordofbook.main.service;

import com.community.dailyrecordofbook.common.config.auth.dto.SessionUser;
import com.community.dailyrecordofbook.common.util.FileUtil;
import com.community.dailyrecordofbook.common.util.SessionUtil;
import com.community.dailyrecordofbook.main.dto.AddBook;
import com.community.dailyrecordofbook.main.entity.BookSlide;
import com.community.dailyrecordofbook.main.repository.BookSlideRepository;
import com.community.dailyrecordofbook.user.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MainService {

    private final BookSlideRepository bookSlideRepository;
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
        return "main";
    }

    public List<BookSlide> getBookList() {
        return bookSlideRepository.findAll();
    }
}
