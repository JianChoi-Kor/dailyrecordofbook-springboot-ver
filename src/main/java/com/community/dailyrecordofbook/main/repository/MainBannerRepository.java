package com.community.dailyrecordofbook.main.repository;

import com.community.dailyrecordofbook.main.entity.MainBanner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MainBannerRepository extends JpaRepository<MainBanner, Long> {
    Optional<MainBanner> findByIdx(Long bannerIdx);
}
