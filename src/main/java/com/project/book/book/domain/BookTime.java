package com.project.book.book.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public enum BookTime {

    before,
    after,
    twoYear,
    fiveYear,
    sevenYear
}
