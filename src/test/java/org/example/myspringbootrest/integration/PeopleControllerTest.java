package org.example.myspringbootrest.integration;

import org.example.myspringbootrest.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

class PeopleControllerTest extends BaseIntegrationTest {

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
    @DisplayName("Метод getPeople() вернет статус 200 и список людей")
    void getPeople_WithGoodRequest_ReturnIsOkAndPeopleList() {
        webTestClient.get().uri("/v1/api/people")
                .header("X-Test-Token", "test-token")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").exists();
    }

    @Test
    @DisplayName("Метод getPerson() вернет статус 200 и информацию о человеке")
    void getPerson_WithGoodRequest_ReturnIsOkAndPersonInfo() {
        webTestClient.get().uri("/v1/api/people/1")
                .header("X-Test-Token", "test-token")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").exists();
    }

    @Test
    @DisplayName("Метод getPerson() вернет статус 404 если человек не найден")
    void getPerson_WithBadRequest_Return404AndErrorMessage() {
        webTestClient.get().uri("/v1/api/people/9999")
                .header("X-Test-Token", "test-token")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND.value())
                .expectBody()
                .jsonPath("$.message").isEqualTo("Человека с таким id не существует")
                .jsonPath("$.timestamp").exists();
    }
}