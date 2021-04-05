package com.bobocode.dao;

import com.bobocode.model.Account;

import java.util.List;

/**
 * Defines an API for {@link Account} data access object (DAO).
 */
public interface AccountDao {
    List<Account> findAll();
}
