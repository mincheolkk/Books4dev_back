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

    public void calculateAvgStar(final double star) {
        this.avgStar = star;
        this.avgStar = ((double) Math.round((this.avgStar * 100 )/ 10))/10;

        this.avgStar = Math.max(0, this.avgStar);
        this.avgStar = Math.min(5, this.avgStar);
    }
}
