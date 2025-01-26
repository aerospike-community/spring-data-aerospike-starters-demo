package com.aerospike.starters.demo;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.IAerospikeClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.aerospike.AerospikeDataProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.aerospike.core.AerospikeTemplate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
// When there is namespace property given
public class SyncDataTests {

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
    void shouldHaveNotOnlyClientBean() { // when there is namespace property given
        assertThat(applicationContext.getBeanProvider(IAerospikeClient.class).stream().count()).isEqualTo(1);
        assertThat(applicationContext.getBeanProvider(AerospikeTemplate.class).stream().count()).isEqualTo(1);
        assertThat(applicationContext.getBeanProvider(AerospikeDataProperties.class).stream().count()).isEqualTo(1);
    }
}
