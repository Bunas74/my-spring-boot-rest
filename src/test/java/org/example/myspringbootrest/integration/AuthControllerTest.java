package org.example.myspringbootrest.integration;

import java.io.IOException;
import org.example.myspringbootrest.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

class AuthControllerTest extends BaseIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer;

    static {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("postgres")
                .withUsername("postgres")
                .withPassword("postgres");
    }

    @DynamicPropertySource
    static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
    }

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Метод login() вернет статус 200 и JWT-token")
    void login_WithGoodRequest_ReturnIsOkAndJwtToken() throws IOException {
        webTestClient.post().uri("/v1/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonMapper("json/AuthControllerIT/good_request_login.json"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.JWT-token").exists();
    }

    @Test
    @DisplayName("Метод login() вернет статус 400 и сообщение об ошибке")
    void login_WithBadRequest_Return400AndErrorMessage() throws IOException {
        webTestClient.post().uri("/v1/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonMapper("json/AuthControllerIT/bad_request.json"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST.value())
                .expectBody()
                .jsonPath("$.message").isEqualTo("password - Пароль не должен быть пустым; ")
                .jsonPath("$.timestamp").exists();
    }

    @Test
    @DisplayName("Метод createNewUser() вернет статус 200 и JWT-token")
    void createNewUser_WithGoodRequest_ReturnIsOkAndJwtToken() throws IOException {
        webTestClient.post().uri("/v1/api/auth/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonMapper("json/AuthControllerIT/good_request.json"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.JWT-token").exists();
    }

    @Test
    @DisplayName("Метод createNewUser() вернет статус 400 и сообщение об ошибке")
    void createNewUser_WithGoodRequest_Return400AndErrorMessage() throws IOException {
        webTestClient.post().uri("/v1/api/auth/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonMapper("json/AuthControllerIT/bad_request_registration.json"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST.value())
                .expectBody()
                .jsonPath("$.message").isEqualTo("username - Человек с таким именем уже существует; ")
                .jsonPath("$.timestamp").exists();
    }
}