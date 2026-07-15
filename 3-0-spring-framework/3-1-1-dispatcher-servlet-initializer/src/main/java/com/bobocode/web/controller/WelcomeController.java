package com.bobocode.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Welcome controller that consists of one method that handles get request to "/welcome" and respond with a message.
 * <p>
 */
@Controller
public class WelcomeController {

    @GetMapping("/welcome")
    @ResponseBody
    public String welcome() {

        return "welcome";
    }
}
