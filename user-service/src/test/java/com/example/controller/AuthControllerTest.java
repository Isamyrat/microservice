package com.example.controller;

import com.example.dto.JwtRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.example.controller.AuthController.LOGIN_URL;
import static com.example.service.impl.AuthenticationServiceImpl.USER_NOT_FOUND;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends AbstractWebTests {

    private final String USER_NAME = "root";
    private final String PASSWORD = "ac8fd58as6dgf584";

    @Test
    void testValidLogin() throws Exception {
        String data = gson.toJson(new JwtRequestDto(USER_NAME, PASSWORD));

        this.mockMvc.perform(post(LOGIN_URL)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(data))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("accessToken")));
    }

    @Test
    void testLoginWithNonExistsUser() throws Exception {
        String data = gson.toJson(new JwtRequestDto("fsfgdg4563aferwrw", PASSWORD));

        this.mockMvc.perform(post(LOGIN_URL)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(data))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString(USER_NOT_FOUND)));
    }

    @Test
    void testLoginWithInvalidUsername() throws Exception {
        String data = gson.toJson(new JwtRequestDto("", PASSWORD));

        this.mockMvc.perform(post(LOGIN_URL)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(data))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("Username can not be empty.")));
    }

    @Test
    void testLoginWithInvalidPassword() throws Exception {
        String data = gson.toJson(new JwtRequestDto(USER_NAME, ""));

        this.mockMvc.perform(post(LOGIN_URL)
                                 .contentType(APPLICATION_JSON)
                                 .content(data))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("Password can not be empty.")));
    }
}
