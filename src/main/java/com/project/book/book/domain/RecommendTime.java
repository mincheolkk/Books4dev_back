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

    public void makeZero() {
        this.beforeCount = 0;
        this.afterCount = 0;
        this.twoYearCount = 0;
        this.fiveYearCount = 0;
        this.tenYearCount = 0;
    }
    public void plusRecommendTime(BookTime time, int count) {
        if (time == BookTime.before) {
            this.beforeCount = count;
        } else if (time == BookTime.after) {
            this.afterCount = count;
        } else if (time == BookTime.twoYear) {
            this.twoYearCount = count;
        } else if (time == BookTime.fiveYear) {
            this.fiveYearCount = count;
        } else if (time == BookTime.tenYear) {
            this.tenYearCount = count;
        }
    }
}
