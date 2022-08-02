package com.bobocode;

import static org.assertj.core.api.Assertions.assertThat;

import com.bobocode.dao.AccountDao;
import com.bobocode.service.AccountService;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApplicationContextTest {
    @Configuration
    @ComponentScan(basePackages = "com.bobocode")
    static class TestConfig {
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @Order(1)
    @DisplayName("DataGenerator has only one bean")
    void dataGeneratorHasOnlyOneBean() {
        Map<String, TestDataGenerator> testDataGeneratorMap = applicationContext.getBeansOfType(TestDataGenerator.class);

        assertThat(testDataGeneratorMap.size()).isEqualTo(1);
    }

    @Test
    @Order(2)
    @DisplayName("DataGenerator bean is called \"dataGenerator\"")
    void testDataGeneratorBeanName() {
        Map<String, TestDataGenerator> dataGeneratorBeanMap = applicationContext.getBeansOfType(TestDataGenerator.class);

        assertThat(dataGeneratorBeanMap.keySet()).contains("dataGenerator");
    }

    @Test
    @Order(3)
    @DisplayName("AccountDao has only one bean")
    void accountDaoHasOnlyOneBean() {
        Map<String, AccountDao> accountDaoBeanMap = applicationContext.getBeansOfType(AccountDao.class);

        assertThat(accountDaoBeanMap.size()).isEqualTo(1);
    }

    @Test
    @Order(4)
    @DisplayName("AccountDao bean is called \"accountDao\"")
    void accountDaoBeanName() {
        Map<String, AccountDao> accountDaoBeanMap = applicationContext.getBeansOfType(AccountDao.class);

        assertThat(accountDaoBeanMap.keySet()).contains("accountDao");
    }

    @Test
    @Order(6)
    @DisplayName("AccountService has only one bean")
    void accountServiceHasOnlyOneBean() {
        Map<String, AccountService> accountServiceMap = applicationContext.getBeansOfType(AccountService.class);

        assertThat(accountServiceMap.size()).isEqualTo(1);
    }

    @Test
    @Order(7)
    @DisplayName("AccountService bean is called \"accountService\"")
    void accountServiceBeanName() {
        Map<String, AccountService> accountServiceMap = applicationContext.getBeansOfType(AccountService.class);

        assertThat(accountServiceMap.keySet()).contains("accountService");
    }
}
