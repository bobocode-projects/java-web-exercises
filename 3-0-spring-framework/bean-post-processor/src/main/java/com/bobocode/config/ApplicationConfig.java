package com.bobocode.config;

import com.bobocode.annotation.EnableStringTrimming;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configure the application for using @Trimmed annotation and corresponding logic.
 */

@Configuration
@ComponentScan(basePackages = "com.bobocode.service")
@EnableStringTrimming
public class ApplicationConfig {

}
