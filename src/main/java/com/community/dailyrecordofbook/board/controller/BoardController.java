package com.community.dailyrecordofbook.board.controller;

import com.community.dailyrecordofbook.board.dto.Write;
import com.community.dailyrecordofbook.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/{boardIdx}")
    public ModelAndView detailAndModify(@PathVariable Long boardIdx, @RequestParam(value = "category") Long categoryIdx, String flag, ModelAndView modelAndView) {
        return boardService.detailAndModify(boardIdx, categoryIdx, flag, modelAndView);
    }

    @GetMapping("/write")
    public String write() {
        return "board/write";
    }

    @PostMapping("/write")
    public String write(Write write) throws Exception {
        return boardService.write(write);
    }

    @PostMapping("/modify")
    public String modify(Write write) throws Exception {
        return boardService.modify(write);
    }


    // CKEditor 이미지 업로드 부분
    @ResponseBody
    @PostMapping("/imageUpload")
    public Map<String, Object> imageUpload(@RequestParam("upload") MultipartFile multipartFile, HttpServletRequest request) throws Exception {
        return boardService.writeImageUpload(multipartFile, request);
    }

    @GetMapping("/list")
    public String getList(@RequestParam(value = "category") Long categoryIdx, @RequestParam(required = false) Integer page, Integer pageSize, Model model) {
        return boardService.getList(categoryIdx, page, pageSize, model);
    }

}
