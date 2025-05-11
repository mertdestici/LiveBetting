package com.betting.livebetting.repository;

import com.betting.livebetting.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {

    @Query("SELECT COALESCE(SUM(b.multiplier), 0) FROM Bet b WHERE b.eventId = :eventId")
    long sumMultiplierByEventId(Long eventId);
}
