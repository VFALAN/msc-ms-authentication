package com.msc.ms.authentification.controllers;

import base.BaseTestConfiguration;
import com.msc.ms.authentification.authentication.model.request.LoginRequestDTO;
import com.msc.ms.authentification.configuration.SecurityConfig;
import com.msc.ms.authentification.log.model.LogPassHistoryRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@Slf4j
@Sql(value = "classpath:scripts/authenticationTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class AuthenticationControllerTest extends BaseTestConfiguration {

    @Autowired
    private MockMvc mockMvc;


    // @Sql(value = "/scripts/authenticationTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void testAuthentication() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        final var request = LoginRequestDTO.builder()
                .username("testUser").password("Hola123$").build();
        mockMvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // @Sql(value = "/scripts/authenticationTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void testWrongPassword() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        final var request = LoginRequestDTO.builder()
                .username("testUser").password("Hola13$").build();
        mockMvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("A002"))
        ;
    }

    // @Sql(value = "/scripts/authenticationTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void testExpiredPassword() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        final var request = LoginRequestDTO.builder()
                .username("passwordExpired").password("Hola123$").build();
        mockMvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("A004"));
    }


    // @Sql(value = "/scripts/authenticationTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void testDisabledUser() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        final var request = LoginRequestDTO.builder()
                .username("disabledUser").password("Hola123$").build();
        mockMvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("A001"));
    }

    // @Sql(value = "/scripts/authenticationTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void testInvalidUsername() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        final var request = LoginRequestDTO.builder()
                .username("testUserNoExisting").password("Hola123$").build();
        mockMvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("A003"));
    }

    // @Sql(value = "/scripts/authenticationTest.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void testNoHistoryLogPass() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        final var request = LoginRequestDTO.builder()
                .username("nohistory").password("Hola123$").build();
        mockMvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("A005"));
    }

    @Test
    void testRegistryPasswordLogHistory() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        final var request = LogPassHistoryRequest.builder().idUser(5).password("i/W0SDWGYwMpZ/wrQKAEVw==").build();
        mockMvc.perform(post("/api/password/log")
                        .header("msc-security-key-header","GTxNoj5MG3xmm39kRC1hkg==")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());
    }
}
