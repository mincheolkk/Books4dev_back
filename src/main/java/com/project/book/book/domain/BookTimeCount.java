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
                return;
            case after:
                this.afterCount += count;
                return;
            case threeYear:
                this.threeYearCount += count;
                return;
            case sixYear:
                this.sixYearCount += count;
                return;
            case anyTime:
                this.anyTimeCount += count;
                return;
        }
    }
}
