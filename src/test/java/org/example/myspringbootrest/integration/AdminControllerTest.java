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

class AdminControllerTest extends BaseIntegrationTest {

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
    @DisplayName("Метод createPerson() вернет статус 200")
    void createPerson_WithGoodRequest_ReturnIsOk() throws IOException {
        webTestClient.post().uri("/v1/api/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Test-Token", "test-token")
                .bodyValue(jsonMapper("json/AdminControllerIT/good_request.json"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Метод createPerson() вернет статус 400 и сообщение об ошибке")
    void createPerson_WithBadRequest_Return400AndErrorMessage() throws IOException {
        webTestClient.post().uri("/v1/api/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Test-Token", "test-token")
                .bodyValue(jsonMapper("json/AdminControllerIT/bad_request.json"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST.value())
                .expectBody()
                .jsonPath("$.message").isEqualTo("role - Роль не должна быть пустой; ")
                .jsonPath("$.timestamp").exists();
    }

    @Test
    @DisplayName("Метод updatePerson() вернет статус 200")
    void updatePerson_WithGoodRequest_ReturnIsOk() throws IOException {
        webTestClient.put().uri("/v1/api/admin/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Test-Token", "test-token")
                .bodyValue(jsonMapper("json/AdminControllerIT/good_request.json"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Метод updatePerson() вернет статус 404 и сообщение об ошибке")
    void updatePerson_WithBadRequest_Return404AndErrorMessage() throws IOException {
        webTestClient.put().uri("/v1/api/admin/{id}", 9L)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Test-Token", "test-token")
                .bodyValue(jsonMapper("json/AdminControllerIT/good_request.json"))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND.value())
                .expectBody()
                .jsonPath("$.message").isEqualTo("Человека с таким id не существует")
                .jsonPath("$.timestamp").exists();
    }

    @Test
    @DisplayName("Метод deletePerson() вернет статус 200")
    void deletePerson_WithGoodId_ReturnIsOk() {
        webTestClient.delete().uri("/v1/api/admin/{id}", 1L)
                .header("X-Test-Token", "test-token")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Метод deletePerson() вернет статус 404 и сообщение об ошибке")
    void deletePerson_WithBadId_ReturnIsNotFound() {
        webTestClient.delete().uri("/v1/api/admin/{id}", 99)
                .header("X-Test-Token", "test-token")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Человека с таким id не существует")
                .jsonPath("$.timestamp").isNotEmpty();
    }
}