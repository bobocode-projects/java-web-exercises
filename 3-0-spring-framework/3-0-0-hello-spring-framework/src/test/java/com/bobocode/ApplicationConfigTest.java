package com.bobocode;

import com.bobocode.config.ApplicationConfig;
import com.bobocode.dao.AccountDao;
import com.bobocode.dao.FakeAccountDao;
import com.bobocode.service.AccountService;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationConfigTest {

    @Test
    @Order(1)
    @DisplayName("Config class is marked as @Configuration")
    void configClassIsMarkedAsConfiguration() {
        Configuration configuration = ApplicationConfig.class.getAnnotation(Configuration.class);

        assertNotNull(configuration);
    }

    @Test
    @Order(2)
    @DisplayName("@ComponentScan is enabled")
    void componentScanIsEnabled() {
        ComponentScan componentScan = ApplicationConfig.class.getAnnotation(ComponentScan.class);

        assertNotNull(componentScan);
    }

    @Test
    @Order(3)
    @DisplayName("@ComponentScan packages are specified")
    void componentScanPackagesAreSpecified() {
        ComponentScan componentScan = ApplicationConfig.class.getAnnotation(ComponentScan.class);
        String[] packages = componentScan.basePackages();
        if (packages.length == 0) {
            packages = componentScan.value();
        }
        assertThat(packages).containsExactlyInAnyOrder("com.bobocode.dao", "com.bobocode.service");
    }

    @Test
    @Order(4)
    @DisplayName("DataGenerator bean is configured in method marked with @Bean")
    void dataGeneratorBeanIsConfiguredExplicitly() {
        Method[] methods = ApplicationConfig.class.getMethods();
        Method testDataGeneratorBeanMethod = findTestDataGeneratorBeanMethod(methods);

        assertNotNull(testDataGeneratorBeanMethod);
    }

    @Test
    @Order(5)
    @DisplayName("DataGenerator bean name is not specified explicitly")
    void dataGeneratorBeanNameIsNotSpecifiedExplicitly() {
        Method[] methods = ApplicationConfig.class.getMethods();
        Method testDataGeneratorBeanMethod = findTestDataGeneratorBeanMethod(methods);
        Bean bean = testDataGeneratorBeanMethod.getDeclaredAnnotation(Bean.class);

        assertThat(bean.name().length).isEqualTo(0);
        assertThat(bean.value().length).isEqualTo(0);
    }

    @Test
    @Order(6)
    @DisplayName("FakeAccountDao is configured as @Component")
    void fakeAccountDaoIsConfiguredAsComponent() {
        Component component = FakeAccountDao.class.getAnnotation(Component.class);

        assertNotNull(component);
    }

    @Test
    @Order(7)
    @DisplayName("AccountService is configured as @Service")
    void accountServiceIsConfiguredAsService() {
        Service service = AccountService.class.getAnnotation(Service.class);

        assertNotNull(service);
    }

    @Test
    @Order(8)
    @DisplayName("AccountService bean name is not specified explicitly")
    void accountServiceBeanNameIsNotSpecifiedExplicitly() {
        Service service = AccountService.class.getAnnotation(Service.class);

        assertThat(service.value()).isEqualTo("");
    }

    @Test
    @Order(9)
    @DisplayName("AccountService doesn't use @Autowired")
    void accountServiceDoesNotUseAutowired() throws NoSuchMethodException {
        Annotation[] annotations = AccountService.class.getConstructor(AccountDao.class).getDeclaredAnnotations();

        assertThat(annotations.length).isEqualTo(0);
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
}
