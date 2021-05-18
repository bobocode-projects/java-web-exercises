package com.bobocode;

import com.bobocode.config.RootConfig;
import com.bobocode.config.WebConfig;
import com.bobocode.dao.AccountDao;
import com.bobocode.dao.impl.InMemoryAccountDao;
import com.bobocode.model.Account;
import com.bobocode.web.controller.AccountRestController;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringJUnitWebConfig(classes = {RootConfig.class, WebConfig.class})
class AccountRestControllerTest {
    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private InMemoryAccountDao accountDao;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        accountDao.clear();
    }

    @Test
    @Order(1)
    @DisplayName("AccountRestController is marked as @RestController")
    void accountRestControllerAnnotation() {
        RestController restController = AccountRestController.class.getAnnotation(RestController.class);

        assertNotNull(restController);
    }

    @Test
    @Order(2)
    @DisplayName("AccountRestController is specified in @RequestMapping")
    void accountRestControllerRequestMapping() {
        RequestMapping requestMapping = AccountRestController.class.getAnnotation(RequestMapping.class);

        assertNotNull(requestMapping);
        assertThat(requestMapping.value().length).isEqualTo(1);
        assertThat(requestMapping.value()).contains("/accounts");
    }

    @Test
    @Order(3)
    @DisplayName("AccountDao is injected using constructor")
    void accountDaoInjection() {
        Constructor<?> constructor = AccountRestController.class.getConstructors()[0];

        assertThat(constructor.getParameterTypes()).contains(AccountDao.class);
    }

    @Test
    @Order(4)
    @DisplayName("Getting all accounts is implemented")
    void getAllAccounts() throws Exception {
        Account account1 = create("Johnny", "Boy", "jboy@gmail.com");
        Account account2 = create("Okko", "Bay", "obay@gmail.com");
        accountDao.save(account1);
        accountDao.save(account2);

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].email").value(hasItems("jboy@gmail.com", "obay@gmail.com")));
    }

    @Test
    @Order(5)
    @DisplayName("Getting all accounts response status is OK")
    void getAccountsResponseStatusCode() throws Exception {
        mockMvc.perform(get("/accounts").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    @DisplayName("Getting account by Id with path variable is implemented")
    void getById() throws Exception {
        Account account = create("Johnny", "Boy", "jboy@gmail.com");
        accountDao.save(account);

        mockMvc.perform(get(String.format("/accounts/%d", account.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(account.getId()))
                .andExpect(jsonPath("$.email").value("jboy@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("Johnny"))
                .andExpect(jsonPath("$.lastName").value("Boy"));
    }

    @Test
    @Order(7)
    @DisplayName("Creating account returns corresponding HTTP status - 201")
    void httpStatusCodeOnCreate() throws Exception {
        mockMvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Johnny\", \"lastName\":\"Boy\", \"email\":\"jboy@gmail.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(8)
    @DisplayName("Creating account returns assigned Id")
    void createAccountReturnsAssignedId() throws Exception {
        mockMvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Johnny\", \"lastName\":\"Boy\", \"email\":\"jboy@gmail.com\"}"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    private Account create(String firstName, String lastName, String email) {
        Account account = new Account();
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);
        return account;
    }

    @Test
    @Order(9)
    @DisplayName("Updating account is implemented")
    void updateAccount() throws Exception {
        Account account = create("Johnny", "Boy", "jboy@gmail.com");
        accountDao.save(account);

        mockMvc.perform(put(String.format("/accounts/%d", account.getId())).contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"id\":\"%d\", \"firstName\":\"Johnny\", \"lastName\":\"Boy\", \"email\":\"johnny.boy@gmail.com\"}", account.getId())))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(10)
    @DisplayName("Removing account is implemented")
    void removeAccount() throws Exception {
        Account account = create("Johnny", "Boy", "jboy@gmail.com");
        accountDao.save(account);

        mockMvc.perform(delete(String.format("/accounts/%d", account.getId())))
                .andExpect(status().isNoContent());
    }
}

