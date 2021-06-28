package com.community.dailyrecordofbook.board.service;

import com.community.dailyrecordofbook.board.dto.DeleteInfo;
import com.community.dailyrecordofbook.board.dto.ListBoard;
import com.community.dailyrecordofbook.board.dto.Write;
import com.community.dailyrecordofbook.board.entity.Board;
import com.community.dailyrecordofbook.board.repository.BoardCustomRepositorySupport;
import com.community.dailyrecordofbook.board.repository.BoardRepository;
import com.community.dailyrecordofbook.common.config.auth.dto.SessionUser;
import com.community.dailyrecordofbook.common.util.FileUtil;
import com.community.dailyrecordofbook.common.util.SessionUtil;
import com.community.dailyrecordofbook.user.entity.Role;
import com.community.dailyrecordofbook.user.entity.User;
import com.community.dailyrecordofbook.user.repository.UserCustomRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
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
        Board board = boardCustomRepositorySupport.findByIdx(write.getIdx());

        String mainImage = getMainImage(write.getContent());
        write.setMainImage(mainImage);
        Board updateBoard = board.updateBoard(write);
        boardRepository.save(updateBoard);
        return "redirect:/board/" + updateBoard.getIdx() + "?flag=detail";
    }

    public int delete(DeleteInfo deleteInfo) {
        User user = userCustomRepositorySupport.findByIdx(deleteInfo.getSessionUserIdx());
        if(ObjectUtils.isEmpty(user)) {
            return 3; // 해당 유저가 없을 경우
        }

        // 해당 글
        Board board = boardCustomRepositorySupport.findByIdx(deleteInfo.getBoardIdx());
        // 관리자가 아닌 경우
        if(user.getRole() != Role.ADMIN) {
            if(board.getWriterIdx() != deleteInfo.getSessionUserIdx()) {
                return 1; // 글 작성자 고유 번호와 접속자 고유 번호가 일치 하지 않는 경우
            }
        }
        try {
            boardRepository.save(board.deleteBoard(board));
        } catch (Exception e) {
            return 2; // 수정 오류
        }
        return 0;
    }

    public int close(DeleteInfo closeInfo) {
        Board board = boardCustomRepositorySupport.findByIdx(closeInfo.getBoardIdx());
        if(board.getCategoryIdx() != 11) {
            return 1; // 모임 모집 카테고리만 해당 기능 작동
        }
        try {
            boardRepository.save(board.closeBoard(board));
        } catch (Exception e) {
            return 2; // 카테고리 변경 오류
        }
        return 0;
    }

    public ModelAndView detailAndModify(Long boardIdx ,String flag, ModelAndView modelAndView) {
        Board board = boardCustomRepositorySupport.findByIdx(boardIdx);
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


    public String getList(Long categoryIdx, Integer page, Model model) {
        if(page == null || page <= 0) {
            page = 0;
        }
        Integer pageSize = 6;
        if(categoryIdx == 11 || categoryIdx == 21) {
            pageSize = 4;
        }

        Pageable pageable = PageRequest.of(page, pageSize);
        PageImpl<ListBoard> listBoards = boardCustomRepositorySupport.getList(categoryIdx, pageable);

        model.addAttribute("pagination", listBoards);

        if(categoryIdx == 1) {
            return "board/list";
        } else if(categoryIdx == 11 || categoryIdx == 12) {
            return "board/communityList";
        } else if(categoryIdx == 21) {
            return "board/notification";
        } else {
            return "main";
        }
    }
}
