package com.sth.jvm.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class MainController {

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        System.out.println("index.......");
        request.setAttribute("ttt", "rrrrr");
        return "index";
    }
}
