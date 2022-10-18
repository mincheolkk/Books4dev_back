package com.project.book.book.domain;

import lombok.*;

import javax.annotation.Nullable;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StarAndCount {

    private double avgStar;

    private Integer registerCount;

    private Integer wishCount;

    public static StarAndCount init() {
        return new StarAndCount(0.0d,0,0);
    }

    public void plusRegisterCount(int count) {
        this.registerCount = count;
    }

    public void plusWishCount(int count) {
        this.wishCount = count;
    }

    //plusRegisterCount 전에 호출할 것
    public void calculateAvgStar(double star) {
        this.avgStar = star;
        this.avgStar = ((double) Math.round((this.avgStar * 100 )/ 10))/10;

    }

}
