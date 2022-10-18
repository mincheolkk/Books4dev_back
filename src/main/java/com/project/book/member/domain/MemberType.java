package com.project.book.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
public enum MemberType {
    BackEnd,
    FrontEnd,
    iOS,
    Android,
    DevOps,
    AInML,
    DATA,
    BlockChian,
    ETC,
    All;

}

//@RequiredArgsConstructor
//@Getter
//public enum MemberType {
//    BACK("B","this is the book enum clasee"),
//    FRONT("F", ), AI, ML
//    private final String code;
//    private final String desc;
//
//
//    }
