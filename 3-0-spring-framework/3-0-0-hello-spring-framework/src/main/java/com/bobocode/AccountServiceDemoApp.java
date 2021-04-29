package com.bobocode;

import com.bobocode.config.ApplicationConfig;
import com.bobocode.model.Account;
import com.bobocode.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * This is a demo application. It uses Spring to create an application context based on the configuration you provided
 * when implementing this exercise. When everything is done and application context created successfully,
 * it will get a "bean" (an object) of a {@link AccountService} from the context. That bean is used to find the richest
 * account and show that everything is working as expected.
 */
public class AccountServiceDemoApp {
    public static void main(String[] args) {
        ApplicationContext context = initializeSpringContext();
        AccountService accountService = context.getBean(AccountService.class);
        Account account = accountService.findRichestAccount();
        System.out.println("The riches person is " + account.getFirstName() + " " + account.getLastName());
    }

    /**
     * In real applications, you will never initialize Spring context manually. We do this here for the sake of demo,
     * so you can see that it's an object that implements {@link ApplicationContext} interface. As you can see,
     * {@link ApplicationContext} has multiple implementations. In this exercise, we use the most popular way of
     * configuring context – Java config with annotations.
     *
     * @return an instance of application context
     */
    private static ApplicationContext initializeSpringContext() {
        return new AnnotationConfigApplicationContext(ApplicationConfig.class);
    }
}
