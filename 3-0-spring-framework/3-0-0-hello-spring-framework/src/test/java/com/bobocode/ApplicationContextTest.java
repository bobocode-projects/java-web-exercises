     package com.bobocode;

import com.bobocode.dao.AccountDao;
import com.bobocode.dao.FakeAccountDao;
import com.bobocode.model.Account;
import com.bobocode.service.AccountService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Comparator;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApplicationContextTest {
    @Configuration
    @ComponentScan(basePackages = "com.bobocode")
    static class TestConfig {
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountDao accountDao;

    @Test
    @Order(1)
    @DisplayName("DataGenerator has only one bean")
    void dataGeneratorHasOnlyOneBean() {
        Map<String, TestDataGenerator> testDataGeneratorMap = applicationContext.getBeansOfType(TestDataGenerator.class);

        assertThat(testDataGeneratorMap.size()).isEqualTo(1);
    }

    @Test
    @Order(2)
    @DisplayName("DataGenerator bean has proper name")
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
    @DisplayName("AccountDao bean has proper name")
    void accountDaoBeanName() {
        Map<String, AccountDao> accountDaoBeanMap = applicationContext.getBeansOfType(AccountDao.class);

        assertThat(accountDaoBeanMap.keySet()).contains("accountDao");
    }

    @Test
    @Order(5)
    @DisplayName("AccountDao constructor is marked with @Autowired")
    void accountDaoConstructorIsMarkedWithAutowired() throws NoSuchMethodException {
        Autowired autowired = FakeAccountDao.class.getConstructor(TestDataGenerator.class).getAnnotation(Autowired.class);

        assertNotNull(autowired);
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
    @DisplayName("AccountService has proper name")
    void accountServiceBeanName() {
        Map<String, AccountService> accountServiceMap = applicationContext.getBeansOfType(AccountService.class);

        assertThat(accountServiceMap.keySet()).contains("accountService");
    }

    @Test
    @Order(8)
    @DisplayName("Find the richest account")
    void findRichestAccount() {
        Account richestAccount = accountService.findRichestAccount();

        Account actualRichestAccount = accountDao.findAll().stream()
                .max(Comparator.comparing(Account::getBalance))
                .orElseThrow();

        assertThat(richestAccount).isEqualTo(actualRichestAccount);
    }
}
