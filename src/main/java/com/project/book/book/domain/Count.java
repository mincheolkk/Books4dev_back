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
public class Count {

    private Integer registerCount;

    private Integer wishCount;

    public static Count init() {
        return new Count(0,0);
    }

    public void plusRegisterCount(final int count) {
        this.registerCount = count;
    }

    public void plusWishCount() {
        this.wishCount += 1;
    }

    public void getWishCount(final int count) {
        this.wishCount = count;
    }

}
