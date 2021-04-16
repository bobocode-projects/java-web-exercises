package com.bobocode;

import com.bobocode.config.RootConfig;
import com.bobocode.config.WebConfig;
import com.bobocode.dao.impl.InMemoryAccountDao;
import com.bobocode.model.Account;
import com.bobocode.web.controller.AccountRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void testAccountRestControllerAnnotation() {
        RestController restController = AccountRestController.class.getAnnotation(RestController.class);

        assertThat(restController, notNullValue());
    }

    @Test
    void testAccountRestControllerRequestMapping() {
        RequestMapping requestMapping = AccountRestController.class.getAnnotation(RequestMapping.class);

        assertThat(requestMapping, notNullValue());
        assertThat(requestMapping.value(), arrayWithSize(1));
        assertThat(requestMapping.value(), arrayContaining("/accounts"));
    }

    @Test
    void testHttpStatusCodeOnCreate() throws Exception {
        mockMvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Johnny\", \"lastName\":\"Boy\", \"email\":\"jboy@gmail.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateAccountReturnsAssignedId() throws Exception {
        mockMvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Johnny\", \"lastName\":\"Boy\", \"email\":\"jboy@gmail.com\"}"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetAccountsResponseStatusCode() throws Exception {
        mockMvc.perform(get("/accounts").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllAccounts() throws Exception {
        Account account1 = create("Johnny", "Boy", "jboy@gmail.com");
        Account account2 = create("Okko", "Bay", "obay@gmail.com");
        accountDao.save(account1);
        accountDao.save(account2);

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].email").value(hasItems("jboy@gmail.com", "obay@gmail.com")));
    }

    private Account create(String firstName, String lastName, String email) {
        Account account = new Account();
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);
        return account;
    }

    @Test
    void testGetById() throws Exception {
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
    void testRemoveAccount() throws Exception {
        Account account = create("Johnny", "Boy", "jboy@gmail.com");
        accountDao.save(account);

        mockMvc.perform(delete(String.format("/accounts/%d", account.getId())))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateAccount() throws Exception {
        Account account = create("Johnny", "Boy", "jboy@gmail.com");
        accountDao.save(account);

        mockMvc.perform(put(String.format("/accounts/%d", account.getId())).contentType(MediaType.APPLICATION_JSON)
                .content(String.format("{\"id\":\"%d\", \"firstName\":\"Johnny\", \"lastName\":\"Boy\", \"email\":\"johnny.boy@gmail.com\"}", account.getId())))
                .andExpect(status().isNoContent());
    }


}
