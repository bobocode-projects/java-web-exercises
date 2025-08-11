package com.bobocode.config;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * This class provides application root (non-web) configuration for a basic spring application context
 * (containing middle-tire services, datasource, etc.).
 * The configuration must exclude the web layer of the application.
 * <p>
 * todo: mark this class as config
 * todo: enable component scanning for all packages in "com.bobocode"
 * todo: ignore all web related config and beans (ignore @{@link Controller}, ignore {@link EnableWebMvc}) using exclude filter
 */
@Configuration
@ComponentScan(basePackages = "com.bobocode",
        excludeFilters = {
                @Filter(
                        type = FilterType.ANNOTATION,
                        classes = EnableWebMvc.class
                ),
                @Filter(
                        type = FilterType.ANNOTATION,
                        classes = Controller.class
                )
        })
public class RootConfig {

}
