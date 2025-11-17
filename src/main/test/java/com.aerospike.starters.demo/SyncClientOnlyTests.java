package com.aerospike.starters.demo;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Key;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.aerospike.AerospikeDataProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.aerospike.core.AerospikeTemplate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
/*
    This test class requires commenting out spring.data.aerospike.namespace in application.properties,
    and also commenting out classes SyncCustomerController and SyncCustomerRepository.
    Only the client bean is loaded when there is no namespace given (only hosts)
*/
public class SyncClientOnlyTests {

    @Autowired
    private AerospikeClient client;
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void printClientPolicy() {
        System.out.println("Java Client ReadPolicyDefault -- sendKey:" + client.getReadPolicyDefault().sendKey);
        System.out.println("Java Client ReadPolicyDefault -- sendKey:" + client.getWritePolicyDefault().sendKey);
    }

    @Test
    void shouldHaveOnlyClientBean() { // when there is no namespace property given
        assertThat(applicationContext.getBeanProvider(IAerospikeClient.class).stream().count()).isEqualTo(1);
        assertThat(applicationContext.getBeanProvider(AerospikeTemplate.class).stream().count()).isEqualTo(0);
        assertThat(applicationContext.getBeanProvider(AerospikeDataProperties.class).stream().count()).isEqualTo(0);
    }

    @Test
    void saveAndReadWithClientOnly() { // when there is no namespace property given
        Key key = new Key("TEST", "customers", "key11");
        Bin bin = new Bin("TestBin1", "Test11");

        client.put(client.getWritePolicyDefault(), key, bin);
        assertThat(client.get(null, key)).isNotNull();
    }
}
