package com.project.book.book.service;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LockRankingService {
    private static final String LOCK_KEY ="RANKING_LOCK";
    private static final int WAIT_TIME = 10;
    private static final int LEASE_TIME = 5;

    private final RedissonClient redissonClient;
    private final RankingService rankingService;

    public LockRankingService(RedissonClient redissonClient, RankingService rankingService) {
        this.redissonClient = redissonClient;
        this.rankingService = rankingService;
    }

    public void record() {
        RLock lock = redissonClient.getLock(LOCK_KEY);

        try {
            boolean available = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);

            if (available) {
                rankingService.scheduleSearchKeywordToRedis();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        } finally {
            if (lock.isHeldByCurrentThread() && lock.isLocked()) {
                lock.unlock();
            }
        }
    }
}
