package com.project.book.book.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.Positive;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Count {

    private Integer readCount;
    private Integer wishCount;
    private Integer commentCount;

    public static Count init() {
        return new Count(0,0,0);
    }

    public void calculateReadCount(final int count) {
        this.readCount += count;
        if (this.readCount < 0) {
            this.readCount = 0;
        }
    }

    public void calculateWishCount(final int count) {
        this.wishCount += count;
        if (this.wishCount < 0) {
            this.wishCount = 0;
        }
    }

    public void calculateCommentCount(final int count) {
        this.commentCount += count;
        if (this.commentCount < 0) {
            this.commentCount = 0;
        }
    }
}
