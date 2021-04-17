package com.bobocode.dao.impl;

import com.bobocode.dao.AccountDao;
import com.bobocode.exception.EntityNotFountException;
import com.bobocode.model.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link AccountDao} implementation that is based on {@link java.util.HashMap}.
 * <p>
 * todo: 1. Configure a component with name "accountDao"
 */
public class InMemoryAccountDao implements AccountDao {
    private Map<Long, Account> accountMap = new HashMap<>();
    private long idSequence = 1L;

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accountMap.values());
    }

    @Override
    public Account findById(long id) {
        Account account = accountMap.get(id);
        if (account == null) {
            throw new EntityNotFountException(String.format("Cannot found account by id = %d", id));
        }
        return account;
    }

    @Override
    public Account save(Account account) {
        if (account.getId() == null) {
            account.setId(idSequence++);
        }
        accountMap.put(account.getId(), account);
        return account;
    }

    @Override
    public void remove(Account account) {
        accountMap.remove(account.getId());
    }

    public void clear() {
        accountMap.clear();
        idSequence = 1L;
    }
}
