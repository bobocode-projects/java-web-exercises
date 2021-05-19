package com.bobocode.web.controller;

import com.bobocode.dao.AccountDao;
import com.bobocode.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * todo: 1. Configure rest controller that handles requests with url "/accounts"
 * todo: 2. Inject {@link AccountDao} implementation
 * todo: 3. Implement method that handles GET request and returns a list of accounts
 * todo: 4. Implement method that handles GET request with id as path variable and returns account by id
 * todo: 5. Implement method that handles POST request, receives account as request body, saves account and returns it
 * todo:    Configure HTTP response status code 201 - CREATED
 * todo: 6. Implement method that handles PUT request with id as path variable and receives account as request body.
 * todo:    It check if account id and path variable are the same and throws {@link IllegalStateException} otherwise.
 * todo:    Then it saves received account. Configure HTTP response status code 204 - NO CONTENT
 * todo: 7. Implement method that handles DELETE request with id as path variable removes an account by id
 * todo:    Configure HTTP response status code 204 - NO CONTENT
 */
@RestController
@RequestMapping("/accounts")
public class AccountRestController {
    private final AccountDao accountDao;

    public AccountRestController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @GetMapping
    public List<Account> getAll() {
        return accountDao.findAll();
    }

    @GetMapping("/{id}")
    public Account getOne(@PathVariable long id) {
        return accountDao.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account create(@RequestBody Account account) {
        return accountDao.save(account);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable long id, @RequestBody Account account) {
        if (!Objects.equals(id, account.getId())) {
            throw new IllegalStateException("Id parameter does not match account body value");
        }
        accountDao.save(account);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable long id) {
        Account account = accountDao.findById(id);
        accountDao.remove(account);
    }
}
