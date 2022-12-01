package chapter.one.LearnSpringBoot.controllers;

import chapter.one.LearnSpringBoot.services.TestDataSetupService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    private final String REGISTER_PAYLOAD_FILEPATH = "src/test/resources/payload/register.json";
    private final String AUTHORIZATION_HEADER = "Authorization";

    // In real world project, call login API again and then use that auth token in same test body
    private static String bearerTokenHeader = "";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private TestDataSetupService setupService;

    void setBearerTokenHeader(MockHttpServletResponse response) {
        if (response.containsHeader(AUTHORIZATION_HEADER)) {
            bearerTokenHeader = response.getHeader(AUTHORIZATION_HEADER);
        }
    }

    HttpHeaders getJSONRequestHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        return httpHeaders;
    }

    HttpHeaders getJSONRequestHeaderWithBearerToken() {
        var httpHeaders = getJSONRequestHeader();
        httpHeaders.set(AUTHORIZATION_HEADER, bearerTokenHeader);
        return httpHeaders;
    }

    MockHttpServletRequestBuilder postRequestBodyFromFile(
            String uri, String reqBodyPath, HttpHeaders headers) throws IOException {
        return post(uri).content(Files.readString(Path.of(reqBodyPath))).headers(headers);
    }

    void assertUserDataPresentInResponse(String resBody, boolean includeSuccess) {
        if (includeSuccess) {
            assertTrue(resBody.contains("\"success\":true"));
        }
        assertTrue(resBody.contains("\"email\":\"user@user.com\""));
        assertTrue(resBody.contains("\"username\":\"user@user.com\""));
        assertTrue(resBody.contains("\"roleName\":\"USER\""));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    void autowiredController() {
        assertNotNull(userController);
    }

    @Test
    @Order(2)
    void getUsersNoAuthToken() throws Exception {
        MvcResult result = this.mockMvc.perform(
                        get("/user")
                                .headers(getJSONRequestHeader()))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    @Order(3)
    void setupUserRoleData() throws Exception {
        MvcResult result = this.mockMvc.perform(
                        post("/user/test/setup/roles")
                                .content("{}")
                                .headers(getJSONRequestHeader()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String resBody = result.getResponse().getContentAsString();
        assertTrue(resBody.contains("\"roleName\":\"USER\""));
        assertTrue(resBody.contains("\"roleName\":\"ADMIN\""));
    }

    @Test
    @Order(4)
    void registerFailMissingAllData() throws Exception {
        MvcResult result = this.mockMvc.perform(
                        post("/user/register")
                                .content("{}")
                                .headers(getJSONRequestHeader()))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andReturn();
    }

    @Test
    @Order(5)
    void registerSuccess() throws Exception {
        MvcResult result = this.mockMvc.perform(
                        postRequestBodyFromFile("/user/register",
                                REGISTER_PAYLOAD_FILEPATH,
                                getJSONRequestHeader()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        assertUserDataPresentInResponse(result.getResponse().getContentAsString(), true);
    }

    @Test
    @Order(6)
    void loginTestFail() throws Exception {
        this.mockMvc.perform(
                        post("/user/login")
                                .content("{}")
                                .headers(getJSONRequestHeader()))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(7)
    void loginTestSuccess() throws Exception {
        // setupService.init(); // create reqd users
        MvcResult result = this.mockMvc.perform(
                        postRequestBodyFromFile(
                                "/user/login",
                                REGISTER_PAYLOAD_FILEPATH,
                                getJSONRequestHeader()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        assertUserDataPresentInResponse(result.getResponse().getContentAsString(), true);
        setBearerTokenHeader(result.getResponse()); // After assertion
    }

    @Test
    @Order(8)
    void getUsersAfterRegister() throws Exception {
        MvcResult result = this.mockMvc.perform(
                        get("/user").headers(getJSONRequestHeaderWithBearerToken()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        assertUserDataPresentInResponse(result.getResponse().getContentAsString(), false);
    }

    @Test
    @Order(9)
    void getUsersAfterRegisterSkip10000() throws Exception {
        MvcResult result = this.mockMvc.perform(
                        get("/user?skip=100000")
                                .headers(getJSONRequestHeaderWithBearerToken()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String resBody = result.getResponse().getContentAsString();
        assertEquals("[]", resBody);
    }
}