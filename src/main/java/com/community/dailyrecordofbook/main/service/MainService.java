package com.community.dailyrecordofbook.main.service;

import com.community.dailyrecordofbook.board.dto.ListBoard;
import com.community.dailyrecordofbook.board.repository.BoardCustomRepositorySupport;
import com.community.dailyrecordofbook.common.config.auth.dto.SessionUser;
import com.community.dailyrecordofbook.common.util.FileUtil;
import com.community.dailyrecordofbook.common.util.SessionUtil;
import com.community.dailyrecordofbook.main.dto.AddBook;
import com.community.dailyrecordofbook.main.entity.BookSlide;
import com.community.dailyrecordofbook.main.entity.MainBanner;
import com.community.dailyrecordofbook.main.entity.WithHistory;
import com.community.dailyrecordofbook.main.repository.BookSlideRepository;
import com.community.dailyrecordofbook.main.repository.MainBannerRepository;
import com.community.dailyrecordofbook.user.entity.Role;
import com.community.dailyrecordofbook.user.entity.User;
import com.community.dailyrecordofbook.user.repository.UserCustomRepositorySupport;
import com.community.dailyrecordofbook.user.repository.UserRepository;
import com.community.dailyrecordofbook.user.repository.WithHistoryRepository;
import com.community.dailyrecordofbook.user.repository.WithHistoryRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MainService {

    private final BookSlideRepository bookSlideRepository;
    private final UserCustomRepositorySupport userCustomRepositorySupport;
    private final FileUtil fileUtil;
    private final MainBannerRepository mainBannerRepository;
    private final BoardCustomRepositorySupport boardCustomRepositorySupport;
    private final WithHistoryRepository withHistoryRepository;
    private final WithHistoryRepositorySupport withHistoryRepositorySupport;

    @Transactional
    public String addBook(AddBook addBook, MultipartFile bookSlideFile, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionUser sessionUser = (SessionUser) SessionUtil.getAttribute("user");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if(sessionUser == null) {
            out.println("<script> alert('???????????? ?????? ???????????????.'); location.href='/main';</script>");
            out.flush();
            return "main";
        }

        if(sessionUser.getRole() != Role.ADMIN) {
            out.println("<script> alert('???????????? ?????? ???????????????.'); location.href='/main';</script>");
            out.flush();
            return "main";
        }

        if(bookSlideFile.isEmpty()) {
            out.println("<script> alert('????????? ????????? ???????????????.'); history.back();</script>");
            out.flush();
            return "main";
        }

        BookSlide bookSlide = new BookSlide(addBook);

        String rootPath = request.getSession().getServletContext().getRealPath("/");
        String date = fileUtil.getDate();

        String basePath = rootPath + "res/image/book/" + date;
        String bookImage = fileUtil.transferTo(bookSlideFile, basePath);

        if(bookImage == null) {
            out.println("<script> alert('?????? ???????????? ??????????????????.'); history.back();</script>");
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

    public int delBook(Long bookIdx, Long loginUserIdx, HttpServletRequest request) throws Exception {
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

        try {
            String rootPath = request.getSession().getServletContext().getRealPath("/");
            String filePath = rootPath + bookSlide.getBookImg();
            String originPath = rootPath + bookSlide.getBookImg().replace("thumb_", "");

            // ????????? ??????
            File file = new File(filePath);
            if(file.exists()) {
                file.delete();
            }
            // ?????? ?????? ??????
            File originFile = new File(originPath);
            if(originFile.exists()) {
                originFile.delete();
            }
            bookSlideRepository.delete(bookSlide);
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    @Transactional
    public String addMainBanner(MainBanner mainBanner, MultipartFile bannerFile, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionUser sessionUser = (SessionUser) SessionUtil.getAttribute("user");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if(sessionUser == null) {
            out.println("<script> alert('???????????? ?????? ???????????????.'); location.href='/main';</script>");
            out.flush();
            return "main";
        }

        if(sessionUser.getRole() != Role.ADMIN) {
            out.println("<script> alert('???????????? ?????? ???????????????.'); location.href='/main';</script>");
            out.flush();
            return "main";
        }

        if(bannerFile.isEmpty()) {
            out.println("<script> alert('????????? ????????? ???????????????.'); history.back();</script>");
            out.flush();
            return "main";
        }

        String rootPath = request.getSession().getServletContext().getRealPath("/");
        String date = fileUtil.getDate();

        String basePath = rootPath + "res/image/banner/" + date;
        String bannerImage = fileUtil.transferTo(bannerFile, basePath);

        if(bannerImage == null) {
            out.println("<script> alert('?????? ???????????? ??????????????????.'); history.back();</script>");
            out.flush();
            return "main";
        }

        String filePath = "/res/image/banner/" + date + "/" + bannerImage;
        mainBanner.setBannerImg(filePath);

        mainBannerRepository.save(mainBanner);
        response.sendRedirect("main");
        return "main";
    }

    public List<MainBanner> getBannerList() {
        return mainBannerRepository.findAll();
    }


    public int delBanner(Long bannerIdx, Long loginUserIdx, HttpServletRequest request) throws Exception {
        User user = userCustomRepositorySupport.findByIdx(loginUserIdx);
        if(ObjectUtils.isEmpty(user)) {
            return 1;
        }
        if(user.getRole() != Role.ADMIN) {
            return 1;
        }

        MainBanner mainBanner = mainBannerRepository.findByIdx(bannerIdx).orElse(null);
        if(mainBanner == null) {
            return 1;
        }

        try {
            String rootPath = request.getSession().getServletContext().getRealPath("/");
            String filePath = rootPath + mainBanner.getBannerImg();
            String originPath = rootPath + mainBanner.getBannerImg().replace("thumb_", "");

            // ????????? ??????
            File file = new File(filePath);
            if(file.exists()) {
                file.delete();
            }
            // ?????? ?????? ??????
            File originFile = new File(originPath);
            if(originFile.exists()) {
                originFile.delete();
            }
            mainBannerRepository.delete(mainBanner);
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    public List<ListBoard> getCommunityList() {
        return boardCustomRepositorySupport.getCommunityList();
    }

    public int addWith(WithHistory withHistory) {
        try {
            withHistoryRepository.save(withHistory);
        } catch (Exception e) {
            return 1;
        }
        return 0;
    }

    public WithHistory getWithList() {
        return withHistoryRepositorySupport.getWithHistory();
    }
}
