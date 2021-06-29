package com.community.dailyrecordofbook.main.repository;

import com.community.dailyrecordofbook.main.entity.BookSlide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookSlideRepository extends JpaRepository<BookSlide, Long> {
    Optional<BookSlide> findByIdx(Long idx);
}
