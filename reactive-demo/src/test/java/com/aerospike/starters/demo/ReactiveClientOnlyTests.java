package com.aerospike.starters.demo;

import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.reactor.AerospikeReactorClient;
import com.aerospike.client.reactor.IAerospikeReactorClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.core.ReactiveAerospikeTemplate;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

/*
    Reactive client-only mode: only the reactive client bean is loaded when there is no namespace
    given (only hosts). This test uses a dedicated configuration that clears the namespace property
    and excludes repository auto-configuration, so it can coexist with ReactiveDataTests.
*/
@SpringBootTest(
        classes = ReactiveClientOnlyTests.ReactiveClientOnlyConfig.class,
        properties = "spring.data.aerospike.namespace="
)
public class ReactiveClientOnlyTests {

    @Configuration
    @EnableAutoConfiguration(exclude = {
            org.springframework.boot.aerospike.reactive.data.AerospikeReactiveRepositoriesAutoConfiguration.class
    })
    static class ReactiveClientOnlyConfig {
    }

    @Autowired
    private AerospikeReactorClient reactorClient;
    @Autowired
    private ApplicationContext applicationContext;
    @Value("${embedded.aerospike.namespace}")
    private String namespace;

    @Test
    void shouldHaveOnlyReactiveClientBean() {
        assertThat(applicationContext.getBeanProvider(IAerospikeReactorClient.class).stream().count()).isEqualTo(1);
        assertThat(applicationContext.getBeanProvider(ReactiveAerospikeTemplate.class).stream().count()).isEqualTo(0);
    }

    @Test
    void saveAndReadWithReactiveClientOnly() {
        Key key = new Key(namespace, "customers", "reactiveKey11");
        Bin bin = new Bin("TestBin1", "ReactiveTest11");

        StepVerifier.create(
                reactorClient.put(reactorClient.getWritePolicyDefault(), key, bin)
                        .then(reactorClient.get(null, key))
        ).assertNext(keyRecord -> assertThat(keyRecord.record).isNotNull())
                .verifyComplete();
    }
}
