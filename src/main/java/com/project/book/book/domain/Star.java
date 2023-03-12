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
public class Star {

    private double avgStar;

    public static Star init() {
        return new Star(0.0d);
    }

    //plusRegisterCount 전에 호출할 것
    public void calculateAvgStar(final double star) {
        this.avgStar = star;
        this.avgStar = ((double) Math.round((this.avgStar * 100 )/ 10))/10;

    }
}
