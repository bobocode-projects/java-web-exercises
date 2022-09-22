package com.bobocode.config;

import com.bobocode.EnableStringTrimming;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableStringTrimming
@ComponentScan(basePackages = "com.bobocode.service")
public class TrimmingEnabledConfig {

}
