package com.community.dailyrecordofbook.like.repository;

import com.community.dailyrecordofbook.like.entity.Liked;
import com.community.dailyrecordofbook.like.repository.impl.LikeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Liked, Long>, LikeRepositoryCustom {
    Optional<Liked> findByBoardIdxAndUserIdxAndCmtIdx(Long boardIdx, Long userIdx, Long cmtIdx);
    List<Liked> findAllByBoardIdx(Long boardIdx);
}
