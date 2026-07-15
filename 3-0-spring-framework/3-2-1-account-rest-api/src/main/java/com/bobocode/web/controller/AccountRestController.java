package com.bobocode.web.controller;

import com.bobocode.dao.AccountDao;
import com.bobocode.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 */
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountRestController {
    private final AccountDao accountDao;

    @GetMapping
    public List<Account> getAccounts() {
        return accountDao.findAll();
    }

    @GetMapping("/{id}")
    public Account getAccount(
            @PathVariable long id
    ) {
        return accountDao.findById(id);
    }

    @PostMapping
    public ResponseEntity<Account> saveUser(
            @RequestBody Account account
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDao.save(account));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateUser(
            @PathVariable long id,
            @RequestBody Account account
    ) {
        if (account.getId() != id) {
            throw new IllegalStateException();
        }
        accountDao.save(account);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Account> deleteUser(
            @PathVariable long id
    ) {
        Account account = accountDao.findById(id);
        accountDao.remove(account);

        return ResponseEntity.noContent().build();
    }

}
