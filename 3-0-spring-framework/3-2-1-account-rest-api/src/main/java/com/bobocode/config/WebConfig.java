package com.bobocode.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * This class provides web (servlet) related configuration.
 * <p>
 * todo: 1. Mark this class as Spring config class
 * todo: 2. Enable web mvc using annotation
 * todo: 3. Enable component scanning for package "web" using annotation value
 */
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.bobocode.web")
public class WebConfig {

}
