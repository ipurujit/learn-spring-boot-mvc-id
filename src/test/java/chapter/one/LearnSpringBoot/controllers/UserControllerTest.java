package chapter.one.LearnSpringBoot.controllers;

import chapter.one.LearnSpringBoot.services.TestDataSetupService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private TestDataSetupService setupService;

    @AfterEach
    void tearDown() {
    }

    @Test
    void autowiredController() {
        assertNotNull(userController);
    }

    @Test
    void getUsers() {
    }

    @Test
    void setupUserRoleData() {
    }

    @Test
    void loginTestFail() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        this.mockMvc.perform(
                post("/user/login").content("{}").headers(httpHeaders))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void loginTestSuccess() throws Exception {
        setupService.init(); // create reqd users
        final String requestBody = "{\"email\":\"user@user.com\",\"password\":\"user@user.com\"}";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        MvcResult result = this.mockMvc.perform(
                        post("/user/login")
                                .content(requestBody)
                                .headers(httpHeaders))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String resBody = result.getResponse().getContentAsString();
        assertTrue(resBody.contains("\"success\":true"));
        assertTrue(resBody.contains("\"email\":\"user@user.com\""));
        assertTrue(resBody.contains("\"username\":\"user@user.com\""));
        assertTrue(resBody.contains("\"roleName\":\"USER\""));
    }
}