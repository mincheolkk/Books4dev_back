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

    public static Count init() {
        return new Count(0,0);
    }

    public void readCount(final int count) {
        this.readCount += count;
    }

    public void plusWishCount() {
        this.wishCount += 1;
    }
}
