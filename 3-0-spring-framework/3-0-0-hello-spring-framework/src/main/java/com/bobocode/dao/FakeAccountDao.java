package com.bobocode.dao;

import com.bobocode.TestDataGenerator;
import com.bobocode.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * {@link FakeAccountDao} implements {@link AccountDao} using fake data. Instead of storing and fetching accounts from
 * some storage, it just generates fake records using {@link TestDataGenerator}.
 * <p>
 * An instance of this class should be created and added to the application context, so it is marked as Spring component.
 * Its bean is called "accountDao". And it uses constructor with explicit autowired annotation in order to inject
 * {@link TestDataGenerator} instance.
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
