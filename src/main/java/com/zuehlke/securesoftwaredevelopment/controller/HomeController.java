package com.zuehlke.securesoftwaredevelopment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String showHome() {
        return "home";
    }

    @GetMapping("/redirect")
    public String redirectTo(@RequestParam("url") String url) {
        if (url == null || url.trim().isEmpty()) {
            return "redirect:/";
        }

        if (url.startsWith("http://") || url.startsWith("https://")) {
            return "redirect:/";
        }

        if (!url.startsWith("/")) {
            url = "/" + url;
        }

        return "redirect:" + url;
    }

}
