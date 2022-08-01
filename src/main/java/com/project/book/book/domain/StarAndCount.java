package com.project.book.book.domain;

import lombok.*;

import javax.annotation.Nullable;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode
public class StarAndCount {

    private double avgStar;

    private Integer registerCount;

    private Integer wishCount;

    public static StarAndCount init() {
        return new StarAndCount(0.0d,0,0);
    }

    public void plusRegisterCount() {
        this.registerCount++;
    }

    public void plusWishCount() {
        this.wishCount++;
    }

    //plusRegisterCount 전에 호출할 것
    public void calculateAvgStar(Integer star) {
        double totalStar = this.avgStar * this.registerCount;
        this.avgStar = (totalStar + star) / (this.registerCount + 1);
        this.avgStar = ((double) Math.round((this.avgStar * 100 )/ 10))/10;
    }

}
