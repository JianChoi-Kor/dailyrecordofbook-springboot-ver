package com.community.dailyrecordofbook.board.service;

import com.community.dailyrecordofbook.board.dto.ListBoard;
import com.community.dailyrecordofbook.board.dto.Write;
import com.community.dailyrecordofbook.board.entity.Board;
import com.community.dailyrecordofbook.board.repository.BoardCustomRepositorySupport;
import com.community.dailyrecordofbook.board.repository.BoardRepository;
import com.community.dailyrecordofbook.common.config.auth.dto.SessionUser;
import com.community.dailyrecordofbook.common.util.FileUtil;
import com.community.dailyrecordofbook.common.util.SessionUtil;
import com.community.dailyrecordofbook.user.entity.User;
import com.community.dailyrecordofbook.user.repository.UserCustomRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final FileUtil fileUtil;
    private final BoardRepository boardRepository;
    private final BoardCustomRepositorySupport boardCustomRepositorySupport;
    private final UserCustomRepositorySupport userCustomRepositorySupport;


    // CKEditor 이미지 업로드 부분
    public Map<String, Object> writeImageUpload(MultipartFile multipartFile, HttpServletRequest request) throws Exception {
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        String date = fileUtil.getDate();
        String basePath = rootPath + "res/image/board/" + date;
        String loadPath = "/res/image/board/" + date;

        String writeImage = null;
        try {
            writeImage = fileUtil.transferTo(multipartFile, true, basePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(writeImage == null) {
            return null;
        }
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("uploaded", 1);
        json.put("fileName", writeImage);
        json.put("url", loadPath + "/" + "t_" + writeImage);
        return json;
    }

    public String write(Write write) throws Exception {
        SessionUser sessionUser = (SessionUser) SessionUtil.getAttribute("user");

        String mainImage = getMainImage(write.getContent());

        write.setHitCount(0);
        write.setUseAt("0");
        write.setWriterIdx(sessionUser.getIdx());
        write.setMainImage(mainImage);

        Board board = boardRepository.save(new Board(write));

        return "redirect:/board/" + board.getIdx() + "?flag=detail&category=" + board.getCategoryIdx();
    }

    public String modify(Write write) {
        Board board = boardCustomRepositorySupport.findByIdxAndCategory(write.getIdx(), write.getCategoryIdx());

        String mainImage = getMainImage(write.getContent());
        write.setMainImage(mainImage);
        Board updateBoard = board.updateBoard(write);
        boardRepository.save(updateBoard);
        return "redirect:/board/" + updateBoard.getIdx() + "?flag=detail";
    }

    public ModelAndView detailAndModify(Long boardIdx, Long categoryIdx ,String flag, ModelAndView modelAndView) {
        Board board = boardCustomRepositorySupport.findByIdxAndCategory(boardIdx, categoryIdx);
        if(board == null) {
            // TODO 해당하는 게시글이 없는 경우 notFound
        } else if (board.getUseAt().equals("1")) {
            // TODO 해당하는 글이 삭제된 경우 notFound
        }

        User user = userCustomRepositorySupport.findByIdx(board.getWriterIdx());
        if(user == null) {
            // TODO 해당하는 유저 정보가 없는 경우
        }
        modelAndView.addObject("boardInfo", board);
        modelAndView.addObject("writerInfo", user);

        if(flag.equals("detail")) {
            modelAndView.setViewName("board/detail");
            return modelAndView;

        } else if(flag.equals("modify")) {
            modelAndView.setViewName("board/modify");
            return modelAndView;

        } else {
            // TODO 에러처리
            modelAndView.setViewName("main");
            return modelAndView;
        }
    }

    private String getMainImage(String content) {
        String startPoint = "/res/image/board/";
        String endPoint = "</figure>";
        String mainImage = null;

        int exist = content.indexOf(startPoint);
        if(exist != -1) {
            int s = content.indexOf(startPoint);
            int e = content.indexOf(endPoint) - 2;
            mainImage = content.substring(s, e);
        }
        return mainImage;
    }


    public String getList(Long categoryIdx, Integer page, Integer pageSize, Model model) {
        if(page == null || page <= 0) {
            page = 0;
        }
        if(pageSize == null || pageSize <= 0) {
            pageSize = 6;
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        PageImpl<ListBoard> listBoards = boardCustomRepositorySupport.getList(categoryIdx, pageable);

        model.addAttribute("pagination", listBoards);
        return "board/list";
    }






}
