package com.bobocode;

import com.bobocode.model.Account;
import com.bobocode.model.Gender;
import com.bobocode.model.jpa.Role;
import com.bobocode.model.jpa.RoleType;
import com.bobocode.model.jpa.User;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class TestDataGenerator {
    public Account generateAccount(){
        Fairy fairy = Fairy.create();
        Person person = fairy.person();
        Random random = new Random();

        Account fakeAccount = new Account();
        fakeAccount.setFirstName(person.getFirstName());
        fakeAccount.setLastName(person.getLastName());
        fakeAccount.setEmail(person.getEmail());
        fakeAccount.setBirthday(LocalDate.of(
                person.getDateOfBirth().getYear(),
                person.getDateOfBirth().getMonthOfYear(),
                person.getDateOfBirth().getDayOfMonth()));
        fakeAccount.setGender(Gender.valueOf(person.getSex().name()));
        fakeAccount.setBalance(BigDecimal.valueOf(random.nextInt(200_000)));
        fakeAccount.setCreationTime(LocalDateTime.now());

        return fakeAccount;
    }

    public User generateUser() {
        User user = generateUserOnly();
        user.addRoles(generateRoleSet());
        return user;
    }

    public User generateUser(RoleType... roleTypes) {
        Set<Role> roles = Stream.of(roleTypes).map(Role::valueOf).collect(Collectors.toSet());
        User user = generateUserOnly();
        user.addRoles(roles);
        return user;
    }

    private User generateUserOnly() {
        Fairy fairy = Fairy.create();
        Person person = fairy.person();

        User user = new User();
        user.setFirstName(person.getFirstName());
        user.setLastName(person.getLastName());
        user.setEmail(person.getEmail());
        user.setBirthday(LocalDate.of(
                person.getDateOfBirth().getYear(),
                person.getDateOfBirth().getMonthOfYear(),
                person.getDateOfBirth().getDayOfMonth()));
        user.setCreationDate(LocalDate.now());
        return user;
    }

    public static Set<Role> generateRoleSet() {
        Random random = new Random();
        Predicate<RoleType> randomPredicate = i -> random.nextBoolean();

        return Stream.of(RoleType.values())
                .filter(randomPredicate)
                .map(Role::valueOf)
                .collect(toSet());
    }
}
