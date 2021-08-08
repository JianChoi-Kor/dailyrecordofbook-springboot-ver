package com.community.dailyrecordofbook.user.repository;

import com.community.dailyrecordofbook.main.entity.WithHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithHistoryRepository extends JpaRepository<WithHistory, Long> {
}
