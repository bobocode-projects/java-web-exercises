package com.bobocode.service;

import com.bobocode.dao.AccountDao;
import com.bobocode.model.Account;
import org.springframework.stereotype.Service;

import static java.util.Comparator.comparing;

/**
 * {@link AccountService} is a very simple service that allows to find the richest person based on data provided to
 * {@link AccountDao}.
 * <p>
 * Since it's a service that should be added to the application context, it is marked as Spring service. It order to get
 * {@link AccountDao} instances, it uses implicit constructor-based injection.
 */
@Service
public class AccountService {
    private final AccountDao accountDao;

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Account findRichestAccount() {
        return accountDao.findAll()
                .stream()
                .max(comparing(Account::getBalance))
                .orElseThrow();
    }
}
