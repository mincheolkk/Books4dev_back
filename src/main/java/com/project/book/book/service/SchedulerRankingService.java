package com.project.book.book.service;

import com.project.book.common.config.aop.DistributedLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerRankingService {

    private final RankingService rankingService;

    public SchedulerRankingService(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @Scheduled(cron = "${schedules.cron.reward.publish}")
    @DistributedLock(key = "ranking")
    public void scheduleSearchKeywordToRedis() {
        rankingService.saveSearchKeywordToRedis();
    }
}
