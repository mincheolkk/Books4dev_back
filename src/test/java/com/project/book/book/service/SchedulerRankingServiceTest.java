package com.project.book.book.service;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;


@SpringBootTest(
        properties = "schedules.cron.reward.publish = * * * * * ?"
)
class SchedulerRankingServiceTest {

    @Autowired
    private SchedulerRankingService schedulerRankingService;

    @MockBean
    private RankingService rankingService;

    @DisplayName("스케줄러의 동작을 확인한다.")
    @Test
    void checkScheduler() {
        // given
        verify(rankingService, times(0)).saveSearchKeywordToRedis();

        // when & then
        Awaitility.await()
                .atMost(3, TimeUnit.SECONDS)
                .untilAsserted(
                        () -> verify(rankingService, times(1)).saveSearchKeywordToRedis()
                );
    }

    @DisplayName("메서드가 호출됐는지 확인한다.")
    @Test
    void scheduleSearchKeywordToRedis() {
        // given
        verify(rankingService, times(0)).saveSearchKeywordToRedis();

        // when
        schedulerRankingService.scheduleSearchKeywordToRedis();

        // then
        verify(rankingService, times(1)).saveSearchKeywordToRedis();
    }
}