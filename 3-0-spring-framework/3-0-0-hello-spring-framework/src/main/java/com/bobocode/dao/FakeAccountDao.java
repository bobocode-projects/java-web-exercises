package com.bobocode.dao;

import com.bobocode.TestDataGenerator;
import com.bobocode.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * This class should be marked with @{@link Component}, thus Spring container will create an instance
 * of {@link FakeAccountDao} class, and will register it the context.
 * <p>
 * todo: configure this class as Spring component with bean name "accountDao"
 * todo: use explicit (with {@link Autowired} annotation) constructor-based dependency injection for specific bean
 */

@Component("accountDao")
public class FakeAccountDao implements AccountDao {
    private List<Account> accounts;

    @Autowired
    public FakeAccountDao(TestDataGenerator testDataGenerator) {
        this.accounts = Stream.generate(testDataGenerator::generateAccount)
                .limit(20)
                .collect(toList());
    }

    @Override
    public List<Account> findAll() {
        return accounts;
    }
}
