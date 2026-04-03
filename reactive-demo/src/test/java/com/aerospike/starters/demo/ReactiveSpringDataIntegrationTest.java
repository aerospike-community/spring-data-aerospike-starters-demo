package com.aerospike.starters.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(
        classes = SpringDataAerospikeExampleApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ReactiveSpringDataIntegrationTest {

    @LocalServerPort
    int port;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @AfterEach
    void tearDown() {
        webTestClient.delete().uri("/reactive/customers")
                .exchange()
                .expectStatus().isOk();
    }

    @Order(0)
    @Test
    void returnsEmptyWhenNoCustomers() {
        webTestClient.get().uri("/reactive/customers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Customer.class).hasSize(0);
    }

    @Order(1)
    @Test
    void savesAndGets() {
        webTestClient.post().uri("/reactive/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new Customer("andrea", "Andrea", "Bocelli", 61))
                .exchange()
                .expectStatus().isOk();

        webTestClient.get().uri("/reactive/customer/andrea")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("andrea")
                .jsonPath("$.age").isEqualTo(61);
    }

    @Order(2)
    @Test
    void savesAndFindsByLastName() {
        webTestClient.post().uri("/reactive/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new Customer("andrea", "Andrea", "Bocelli", 61))
                .exchange()
                .expectStatus().isOk();

        webTestClient.get().uri("/reactive/customers/search?lastName=Bocelli")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("andrea")
                .jsonPath("$[0].age").isEqualTo(61);
    }
}
