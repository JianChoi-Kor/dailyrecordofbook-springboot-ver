package com.community.dailyrecordofbook.like.controller;

import com.community.dailyrecordofbook.like.entity.Liked;
import com.community.dailyrecordofbook.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/liked")
@RestController
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public int addLike(@RequestBody Liked liked) {
        return likeService.addLike(liked);
    }

    @DeleteMapping
    public int delLike(Liked liked) {
        return likeService.delLike(liked);
    }

    @GetMapping
    public List<Liked> getLikeList(@RequestParam Long boardIdx) {
        return likeService.getLikeList(boardIdx);
    }

    @GetMapping("/total")
    public Long countLike(@RequestParam Long boardIdx, @RequestParam Long cmtIdx) {
        return likeService.getCount(boardIdx, cmtIdx);
    }
}
