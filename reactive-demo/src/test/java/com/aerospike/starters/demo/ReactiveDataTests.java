package com.aerospike.starters.demo;

import com.aerospike.client.reactor.AerospikeReactorClient;
import com.aerospike.client.reactor.IAerospikeReactorClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.aerospike.core.ReactiveAerospikeTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReactiveDataTests {

    @Autowired
    private AerospikeReactorClient reactorClient;
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void shouldHaveReactiveClientAndTemplateBeans() {
        assertThat(applicationContext.getBeanProvider(IAerospikeReactorClient.class).stream().count()).isEqualTo(1);
        assertThat(applicationContext.getBeanProvider(ReactiveAerospikeTemplate.class).stream().count()).isEqualTo(1);
    }
}
