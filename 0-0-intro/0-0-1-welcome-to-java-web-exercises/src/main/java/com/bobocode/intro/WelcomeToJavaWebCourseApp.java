package com.bobocode.intro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
@Controller
public class WelcomeToJavaWebCourseApp {

    public static void main(String[] args) {
        SpringApplication.run(WelcomeToJavaWebCourseApp.class, args);
    }

    @GetMapping({"", "/welcome"})
    public String welcome(@RequestParam(value = "name", required = false) String name, Model model) {
        model.addAttribute("name", name);
        return "welcome";
    }
}
