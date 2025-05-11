package com.betting.livebetting.repository;

import com.betting.livebetting.model.MatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {
}