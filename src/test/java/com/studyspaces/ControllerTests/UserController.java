package com.studyspaces.ControllerTests;

import com.studyspaces.spacefinder.SpacefinderApplication;
import com.studyspaces.spacefinder.controller.UserController;
import com.studyspaces.spacefinder.service.UserManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = SpacefinderApplication.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserManager userManager;

    // ===========================
    // LOGIN TESTS
    // ===========================

    @Test
    void shouldLoginSuccessfully() throws Exception {
        Map<String, String> loginData = Map.of("username", "testuser", "password", "pass123");
        when(userManager.checkLogin("testuser", "pass123")).thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"pass123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"));

        verify(userManager).checkLogin("testuser", "pass123");
    }

    @Test
    void shouldFailLoginWithWrongPassword() throws Exception {
        when(userManager.checkLogin("testuser", "wrong")).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));

        verify(userManager).checkLogin("testuser", "wrong");
    }

    @Test
    void shouldFailLoginWithUnknownUser() throws Exception {
        when(userManager.checkLogin("unknown", "pass")).thenThrow(new RuntimeException("User not found!"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"unknown\",\"password\":\"pass\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));

        verify(userManager).checkLogin("unknown", "pass");
    }

    // ===========================
    // SIGNUP TESTS
    // ===========================

    @Test
    void shouldSignupSuccessfully() throws Exception {
        Map<String, String> signupData = Map.of("username", "newuser", "password", "123", "email", "new@domain.com");
        when(userManager.signUp(signupData)).thenReturn(true);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newuser\",\"password\":\"123\",\"email\":\"new@domain.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));

        verify(userManager).signUp(signupData);
    }

    @Test
    void shouldFailSignupWhenUsernameTaken() throws Exception {
        Map<String, String> signupData = Map.of("username", "existing", "password", "123", "email", "exist@domain.com");
        when(userManager.signUp(signupData)).thenReturn(false);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"existing\",\"password\":\"123\",\"email\":\"exist@domain.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username already taken"));

        verify(userManager).signUp(signupData);
    }

    @Test
    void shouldHandleSignupException() throws Exception {
        Map<String, String> signupData = Map.of("username", "error", "password", "123", "email", "error@domain.com");
        when(userManager.signUp(signupData)).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"error\",\"password\":\"123\",\"email\":\"error@domain.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unexpected Error"));

        verify(userManager).signUp(signupData);
    }
}