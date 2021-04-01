package com.bobocode;

import com.bobocode.config.AppConfig;
import com.bobocode.dao.AccountDao;
import com.bobocode.dao.FakeAccountDao;
import com.bobocode.model.Account;
import com.bobocode.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


@SpringJUnitConfig
class AppConfigTest {
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
    void testConfigClassIsMarkedAsConfiguration() {
        Configuration configuration = AppConfig.class.getAnnotation(Configuration.class);

        assertThat(configuration, notNullValue());
    }

    @Test
    void testComponentScanIsEnabled() {
        ComponentScan componentScan = AppConfig.class.getAnnotation(ComponentScan.class);

        assertThat(componentScan, notNullValue());
    }

    @Test
    void testComponentScanPackagesAreSpecified() {
        ComponentScan componentScan = AppConfig.class.getAnnotation(ComponentScan.class);
        String[] packages = componentScan.basePackages();
        if (packages.length == 0) {
            packages = componentScan.value();
        }
        assertThat(packages, arrayContainingInAnyOrder("com.bobocode.dao", "com.bobocode.service"));
    }

    @Test
    void testDataGeneratorHasOnlyOneBean() {
        Map<String, TestDataGenerator> testDataGeneratorMap = applicationContext.getBeansOfType(TestDataGenerator.class);

        assertThat(testDataGeneratorMap.size(), is(1));
    }

    @Test
    void testDataGeneratorBeanIsConfiguredExplicitly() {
        Method[] methods = AppConfig.class.getMethods();
        Method testDataGeneratorBeanMethod = findTestDataGeneratorBeanMethod(methods);


        assertThat(testDataGeneratorBeanMethod, notNullValue());
    }

    @Test
    void testDataGeneratorBeanName() {
        Map<String, TestDataGenerator> dataGeneratorBeanMap = applicationContext.getBeansOfType(TestDataGenerator.class);

        assertThat(dataGeneratorBeanMap.keySet(), hasItem("dataGenerator"));
    }

    @Test
    void testDataGeneratorBeanNameIsNotSpecifiedExplicitly() {
        Method[] methods = AppConfig.class.getMethods();
        Method testDataGeneratorBeanMethod = findTestDataGeneratorBeanMethod(methods);
        Bean bean = testDataGeneratorBeanMethod.getDeclaredAnnotation(Bean.class);

        assertThat(bean.name(), arrayWithSize(0));
        assertThat(bean.value(), arrayWithSize(0));
    }

    private Method findTestDataGeneratorBeanMethod(Method[] methods) {
        for (Method method : methods) {
            if (method.getReturnType().equals(TestDataGenerator.class)
                    && method.getDeclaredAnnotation(Bean.class) != null) {
                return method;
            }
        }
        return null;
    }

    @Test
    void testFakeAccountDaoIsConfiguredAsComponent() {
        Component component = FakeAccountDao.class.getAnnotation(Component.class);

        assertThat(component, notNullValue());
    }

    @Test
    void testAccountDaoHasOnlyOneBean() {
        Map<String, AccountDao> accountDaoBeanMap = applicationContext.getBeansOfType(AccountDao.class);

        assertThat(accountDaoBeanMap.size(), is(1));
    }

    @Test
    void testAccountDaoBeanName() {
        Map<String, AccountDao> accountDaoBeanMap = applicationContext.getBeansOfType(AccountDao.class);

        assertThat(accountDaoBeanMap.keySet(), hasItem("accountDao"));
    }

    @Test
    void testAccountDaoConstructorIsMarkedWithAutowired() throws NoSuchMethodException {
        Autowired autowired = FakeAccountDao.class.getConstructor(TestDataGenerator.class).getAnnotation(Autowired.class);

        assertThat(autowired, notNullValue());
    }

    @Test
    void testAccountServiceHasOnlyOneBean() {
        Map<String, AccountService> accountServiceMap = applicationContext.getBeansOfType(AccountService.class);

        assertThat(accountServiceMap.size(), is(1));
    }

    @Test
    void testAccountServiceIsConfiguredAsService() {
        Service service = AccountService.class.getAnnotation(Service.class);

        assertThat(service, notNullValue());
    }

    @Test
    void testAccountServiceBeanName() {
        Map<String, AccountService> accountServiceMap = applicationContext.getBeansOfType(AccountService.class);

        assertThat(accountServiceMap.keySet(), hasItem("accountService"));
    }

    @Test
    void testAccountServiceBeanNameIsNotSpecifiedExplicitly() {
        Service service = AccountService.class.getAnnotation(Service.class);

        assertThat(service.value(), equalTo(""));
    }

    @Test
    void testAccountServiceDoesNotUseAutowired() throws NoSuchMethodException {
        Annotation[] annotations = AccountService.class.getConstructor(AccountDao.class).getDeclaredAnnotations();

        assertThat(annotations, arrayWithSize(0));
    }

    @Test
    void testFindRichestAccount() {
        Account richestAccount = accountService.findRichestAccount();

        Account actualRichestAccount = accountDao.findAll().stream().max(Comparator.comparing(Account::getBalance)).get();

        assertThat(richestAccount, equalTo(actualRichestAccount));
    }


}
