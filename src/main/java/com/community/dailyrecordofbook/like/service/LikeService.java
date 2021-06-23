package com.community.dailyrecordofbook.like.service;

import com.community.dailyrecordofbook.comment.entity.Comment;
import com.community.dailyrecordofbook.comment.repository.CommentRepository;
import com.community.dailyrecordofbook.like.entity.Liked;
import com.community.dailyrecordofbook.like.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public int addLike(Liked liked) {
        Liked like = likeRepository.findByBoardIdxAndUserIdxAndCmtIdx(liked.getBoardIdx(), liked.getUserIdx(), liked.getCmtIdx()).orElse(null);
        if(like == null) {
            likeRepository.save(liked);
            Comment comment = commentRepository.findByIdxAndBoardIdx(liked.getCmtIdx(), liked.getBoardIdx()).orElse(null);
            if(comment != null) {
                comment.setLikeTotal(comment.getLikeTotal() + 1);
                commentRepository.save(comment);
            }
            return 0;
        } else {
            return 1;
        }
    }

    public int delLike(Liked liked) {
        Liked like = likeRepository.findByBoardIdxAndUserIdxAndCmtIdx(liked.getBoardIdx(), liked.getUserIdx(), liked.getCmtIdx()).orElse(null);
        if(like != null) {
            likeRepository.delete(like);
            Comment comment = commentRepository.findByIdxAndBoardIdx(like.getCmtIdx(), like.getBoardIdx()).orElse(null);
            if(comment != null) {
                comment.setLikeTotal(comment.getLikeTotal() - 1);
                commentRepository.save(comment);
            }
            return 0;
        } else {
            return 1;
        }
    }

    public List<Liked> getLikeList(Long boardIdx) {
        return likeRepository.findAllByBoardIdx(boardIdx);
    }

    public Long getCount(Long boardIdx, Long cmtIdx) {
        return likeRepository.getCount(boardIdx, cmtIdx);
    }
}
