package com.bobocode.config;

import com.bobocode.TestDataGenerator;
import com.bobocode.dao.AccountDao;
import com.bobocode.dao.FakeAccountDao;
import com.bobocode.model.Account;
import com.bobocode.service.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * This class specifies application context configuration. It tells Spring to scan "dao" and "service" packages in order
 * to find and create instances for {@link FakeAccountDao} and
 * {@link AccountService}.
 * <p>
 * It also explicitly configures a bean of {@link TestDataGenerator} called "dataGenerator". This beans will be injected
 * into {@link FakeAccountDao} in order to generate some fake accounts.
 */
@Configuration
@ComponentScan(basePackages = {"com.bobocode.dao", "com.bobocode.service"})
public class ApplicationConfig {
    @Bean
    public TestDataGenerator dataGenerator() {
        return new TestDataGenerator();
    }

    @Bean
    public AccountDao accountDao() {
        return new FakeAccountDao(dataGenerator());
    }

    @Bean
    public AccountService accountService() {
        return new AccountService(accountDao());
    }

}
