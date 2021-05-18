package com.bobocode.dao;

import com.bobocode.model.Account;

import java.util.List;

public interface AccountDao {
    List<Account> findAll();

    Account findById(long id);

    Account save(Account account);

    void remove(Account account);
}
