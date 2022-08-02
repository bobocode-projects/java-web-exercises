package com.bobocode;

import com.bobocode.model.Account;
import com.bobocode.model.Gender;
import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class TestDataGenerator {

    public Account generateAccount() {
        Fairy fairy = Fairy.create();
        Person person = fairy.person();

        Account account = convertToAccount(person);
        BigDecimal balance = BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(200_000));
        account.setBalance(balance);
        account.setCreationTime(LocalDateTime.now());

        return account;
    }

    private Account convertToAccount(Person person) {
        Account account = new Account();
        account.setFirstName(person.getFirstName());
        account.setLastName(person.getLastName());
        account.setEmail(person.getEmail());
        account.setBirthday(LocalDate.of(
                person.getDateOfBirth().getYear(),
                person.getDateOfBirth().getMonth(),
                person.getDateOfBirth().getDayOfMonth()));
        account.setGender(Gender.valueOf(person.getSex().name()));
        return account;
    }
}
