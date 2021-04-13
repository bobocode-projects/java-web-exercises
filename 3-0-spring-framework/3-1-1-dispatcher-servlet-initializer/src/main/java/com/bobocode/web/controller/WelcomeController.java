package com.bobocode.web.controller;

/**
 * Welcome controller that consists of one method that handles get request to "/welcome" and respond with a message.
 * <p>
 * todo: mark this class as Spring controller
 * todo: configure HTTP GET mapping "/welcome" for method {@link WelcomeController#welcome()}
 * todo: tell Spring that {@link WelcomeController#welcome()} method provides response body without view
 */

public class WelcomeController {

    public String welcome() {
        return "Welcome to Spring MVC!";
    }
}
