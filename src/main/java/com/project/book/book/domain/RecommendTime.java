package com.project.book.book.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RecommendTime {

    private Integer beforeCount;
    private Integer afterCount;
    private Integer twoYearCount;
    private Integer fiveYearCount;
    private Integer tenYearCount;

    public static RecommendTime init() {
        return new RecommendTime(0, 0, 0, 0, 0);
    }

    public void calculateRecommendTime(final BookTime time, final int count) {
        switch (time) {
            case before:
                this.beforeCount += count;
                return;
            case after:
                this.afterCount += count;
                return;
            case twoYear:
                this.twoYearCount += count;
                return;
            case fiveYear:
                this.fiveYearCount += count;
                return;
            case tenYear:
                this.tenYearCount += count;
                return;
        }
    }
}
