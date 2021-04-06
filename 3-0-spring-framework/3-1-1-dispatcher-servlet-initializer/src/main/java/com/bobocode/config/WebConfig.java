package com.bobocode.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * This class provides web (servlet) related configuration. In the Web MVC framework,
 * each DispatcherServlet has its own WebApplicationContext, which inherits all the beans already defined
 * in the root ApplicationContext.
 * <p>
 * todo: mark this class as Spring config class
 * todo: enable web mvc using annotation
 * todo: enable component scanning for package "web"
 */

public class WebConfig {
}
