package com.project.book.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Healthcontroller {

    @GetMapping
    public String healthCheck() {
        return "health ok";
    }
}
