package com.project.book.book.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerRankingService {

    private final LockRankingService lockRankingService;

    public SchedulerRankingService(LockRankingService lockRankingService) {
        this.lockRankingService = lockRankingService;
    }

    @Scheduled(cron = "0 0 11,23 * * *")
    public void scheduleSearchKeywordToRedis() {
        lockRankingService.record();
    }
}
