package com.project.book.book.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BookTimeCount {

    private Integer beforeCount;
    private Integer afterCount;
    private Integer threeYearCount;
    private Integer sixYearCount;
    private Integer anyTimeCount;

    public static BookTimeCount init() {
        return new BookTimeCount(0, 0, 0, 0, 0);
    }

    public void calculateBookTimeCount(final BookTime time, final int count) {
        switch (time) {
            case before:
                this.beforeCount += count;
                this.beforeCount = Math.max(0, this.beforeCount);
                return;
            case after:
                this.afterCount += count;
                this.afterCount = Math.max(0, this.afterCount);
                return;
            case threeYear:
                this.threeYearCount += count;
                this.threeYearCount = Math.max(0, this.threeYearCount);
                return;
            case sixYear:
                this.sixYearCount += count;
                this.sixYearCount = Math.max(0, this.sixYearCount);
                return;
            case anyTime:
                this.anyTimeCount += count;
                this.anyTimeCount = Math.max(0, this.anyTimeCount);
                return;
        }
    }
}
