package com.aerospike.starters.demo;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Key;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.aerospike.AerospikeDataProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.core.AerospikeTemplate;

import static org.assertj.core.api.Assertions.assertThat;


/*
    Client-only mode: only the client bean is loaded when there is no namespace given (only hosts).
    This test uses a dedicated configuration that clears the namespace property and excludes
    repository auto-configuration, so it can coexist with SyncDataTests and SpringDataIntegrationTest.

    To run this test with a standalone Aerospike Server Docker image instead of the embedded testcontainer:
    - change spring.aerospike.hosts in application.properties (e.g. localhost:3000)
    - change spring.data.aerospike.namespace in application.properties (e.g. test)
    - set embedded.containers.enabled=false in bootstrap.properties
*/
@SpringBootTest(
        classes = SyncClientOnlyTests.ClientOnlyConfig.class,
        properties = "spring.data.aerospike.namespace="
)
public class SyncClientOnlyTests {

    @Configuration
    @EnableAutoConfiguration(exclude = {
            org.springframework.boot.aerospike.data.AerospikeRepositoriesAutoConfiguration.class
    })
    static class ClientOnlyConfig {
    }

    @Autowired
    private AerospikeClient client;
    @Autowired
    private ApplicationContext applicationContext;
    @Value("${embedded.aerospike.namespace}")
    private String namespace;

    @Test
    void printClientPolicy() {
        System.out.println("Java Client ReadPolicyDefault -- sendKey:" + client.getReadPolicyDefault().sendKey);
        System.out.println("Java Client ReadPolicyDefault -- sendKey:" + client.getWritePolicyDefault().sendKey);
    }

    @Test
    void shouldHaveOnlyClientBean() {
        assertThat(applicationContext.getBeanProvider(IAerospikeClient.class).stream().count()).isEqualTo(1);
        assertThat(applicationContext.getBeanProvider(AerospikeTemplate.class).stream().count()).isEqualTo(0);
        assertThat(applicationContext.getBeanProvider(AerospikeDataProperties.class).stream().count()).isEqualTo(0);
    }

    @Test
    void saveAndReadWithClientOnly() {
        Key key = new Key(namespace, "customers", "key11");
        Bin bin = new Bin("TestBin1", "Test11");

        client.put(client.getWritePolicyDefault(), key, bin);
        assertThat(client.get(null, key)).isNotNull();
    }
}
