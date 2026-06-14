package org.esrakonya.backend.auth;

import com.jayway.jsonpath.JsonPath;
import org.esrakonya.backend.common.test.FullInfrastructureTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthIntegrationTest extends FullInfrastructureTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String userAccessToken;

    @Test
    @Order(1)
    @DisplayName("Should register a new user successfully")
    void shouldRegisterUser() throws Exception {
        Map<String, String> registerRequest = Map.of(
                "firstName", "Test",
                "lastName", "Test",
                "email", "test@example.com",
                "password", "password123"
        );

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    @DisplayName("Should login and capture real token")
    void shouldLoginSuccessfully() throws Exception {
        Map<String, String> request = Map.of(
                "email", "test@example.com",
                "password", "password123"
        );

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        userAccessToken = JsonPath.read(response, "$.accessToken");
    }

    @Test
    @Order(3)
    @DisplayName("Should not allow duplicate email registration")
    void shouldNotRegisterDuplicateEmail() throws Exception {
        Map<String, String> request = Map.of(
                "email", "test@example.com",
                "password", "password123"
        );

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(4)
    @DisplayName("Should deny access to protected endpoint without token")
    void shouldDenyUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(5)
    @DisplayName("User should be forbidden from accessing admin-only content")
    void userShouldNotAccessAdminEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/admin/dashboard")
                .header("Authorization", "Bearer " + userAccessToken))
                .andExpect(status().isForbidden());
    }

}
