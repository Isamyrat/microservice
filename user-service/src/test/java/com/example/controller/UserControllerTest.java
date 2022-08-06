package com.example.controller;

import com.example.dto.UserDto;
import com.example.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.example.controller.UserController.USER_URL;
import static com.example.jwt.JwtUtils.BEARER_PREFIX;
import static com.example.util.ConstantsHolder.SECRET;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerTest extends AbstractWebTests {

    private static final String USER = "USER";
    private static final String ADMIN_TEST = "root";
    private static final String INVALID_USER_NAME = "marina_test";
    private static final String USER_NAME = "amina";
    private static final String USER_NAME_FOR_UPDATE = "amina22";
    private static final String USERNAME_TEST = "admin_test";
    private static final String ADMIN_ROLE = "ADMIN";

    @Test
    void testAddUserWithExistUsername() throws Exception {
        String data = gson.toJson(new UserDto(USERNAME_TEST, USER_PASSWORD, ADMIN_ROLE));

        this.mockMvc.perform(post(USER_URL)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(data)
                                 .header(AUTHORIZATION, BEARER_PREFIX + ADMIN_TOKEN))

            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void testAddValidUser() throws Exception {
        String data = gson.toJson(new UserDto(USER_NAME, USER_PASSWORD, USER));
        this.mockMvc.perform(post(USER_URL)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(data)
                                 .header(AUTHORIZATION, BEARER_PREFIX + ADMIN_TOKEN))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is(USER_NAME)));
    }

    @Test
    void testUpdateUserWithExistUsername() throws Exception {
        String data = gson.toJson(new UserDto(ADMIN_TEST, USER_PASSWORD, USER));

        this.mockMvc.perform(put(USER_URL + "/3")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(data)
                                 .header(AUTHORIZATION, BEARER_PREFIX + ADMIN_TOKEN))

            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void testFindAllUsers() throws Exception {
        this.mockMvc.perform(get(USER_URL)
                                 .header(AUTHORIZATION, BEARER_PREFIX + ADMIN_TOKEN))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].username", is("root")))
            .andExpect(jsonPath("$[1].username", is("user_root")));
    }

    @Test
    void testFindUserById() throws Exception {
        this.mockMvc.perform(get(USER_URL + "/1")
                                 .header(AUTHORIZATION, BEARER_PREFIX + ADMIN_TOKEN))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testWithNullUsername() throws Exception {
        String data = gson.toJson(new UserDto("", USER_PASSWORD, USER));

        this.mockMvc.perform(post(USER_URL)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(data)
                                 .header(AUTHORIZATION, BEARER_PREFIX + ADMIN_TOKEN))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void testWithNullPassword() throws Exception {
        String data = gson.toJson(new UserDto(INVALID_USER_NAME, "", USER));

        this.mockMvc.perform(post(USER_URL)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(data)
                                 .header(AUTHORIZATION, BEARER_PREFIX + ADMIN_TOKEN))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void testWithNullRole() throws Exception {
        String data = gson.toJson(new UserDto(INVALID_USER_NAME, USER_PASSWORD, ""));

        this.mockMvc.perform(post(USER_URL)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(data)
                                 .header(AUTHORIZATION, BEARER_PREFIX + ADMIN_TOKEN))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void testWhenUnauthorized() throws Exception {
        this.mockMvc.perform(get(USER_URL))
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }

    @Test
    void testWithNonFoundRole() throws Exception {
        String data = gson.toJson(new UserDto(INVALID_USER_NAME, USER_PASSWORD, "asd"));

        this.mockMvc.perform(post(USER_URL)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(data)
                                 .header(AUTHORIZATION, BEARER_PREFIX + ADMIN_TOKEN))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void testExpiredToken() throws Exception {
        String expiredToken = JwtUtils.generateToken(ADMIN_TEST, SECRET, 1);
        this.mockMvc.perform(get(USER_URL)
                                 .header(AUTHORIZATION, BEARER_PREFIX + expiredToken))
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }


    @Test
    void testUpdateUser() throws Exception {
        String data = gson.toJson(new UserDto(USER_NAME_FOR_UPDATE, USER_PASSWORD, USER));

        this.mockMvc.perform(put(USER_URL + "/2")
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(data)
                                 .header(AUTHORIZATION, BEARER_PREFIX + ADMIN_TOKEN))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is(USER_NAME_FOR_UPDATE)));
    }


}
