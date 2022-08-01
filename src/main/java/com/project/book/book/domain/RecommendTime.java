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

    public void plusRecommendTime(BookTime time) {
        if (time == BookTime.before) {
            this.beforeCount++;
        } else if (time == BookTime.after) {
            this.afterCount++;
        } else if (time == BookTime.twoYear) {
            this.twoYearCount++;
        } else if (time == BookTime.fiveYear) {
            this.fiveYearCount++;
        } else if (time == BookTime.tenYear) {
            this.tenYearCount++;
        }
    }
}
