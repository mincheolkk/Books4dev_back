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
public class Count {

    private Integer readCount;

    private Integer wishCount;

    private Integer commentCount;

    public static Count init() {
        return new Count(0,0,0);
    }

    public void calculateReadCount(final int count) {
        this.readCount += count;
    }

    public void calculateWishCount(final int count) {
        this.wishCount += count;
    }

    public void calculateCommentCount(final int count) {
        this.commentCount += count;
    }
}
