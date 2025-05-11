package com.betting.livebetting.util;
import com.betting.livebetting.model.MatchEvent;
import com.betting.livebetting.repository.MatchEventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Component
public class OddsUpdateScheduler {

    private final MatchEventRepository matchEventRepository;
    private final Random random = new Random();

    public OddsUpdateScheduler(MatchEventRepository matchEventRepository) {
        this.matchEventRepository = matchEventRepository;
    }

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void updateOdds() {
        List<MatchEvent> events = matchEventRepository.findAll();

        for (MatchEvent event : events) {
            event.setHomeWinOdds(applyRandomFactor(event.getHomeWinOdds()));
            event.setDrawOdds(applyRandomFactor(event.getDrawOdds()));
            event.setAwayWinOdds(applyRandomFactor(event.getAwayWinOdds()));
        }

        matchEventRepository.saveAll(events);
    }

    private double applyRandomFactor(double currentOdds) {
        double factor = 0.9 + (1.1 - 0.9) * random.nextDouble();
        return Math.round(currentOdds * factor * 100.0) / 100.0;
    }
}
