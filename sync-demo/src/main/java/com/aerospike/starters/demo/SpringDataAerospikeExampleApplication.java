package com.aerospike.starters.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringDataAerospikeExampleApplication {

/*
    In order to test the application with local Aerospike Server Docker image using REST API:
    - use the following configuration in application.properties:
        spring.aerospike.hosts=localhost:3000
        spring.data.aerospike.namespace=test
        spring.data.aerospike.scans-enabled=false
        spring.aerospike.write.send-key=true
    - use the following configuration in bootstrap.properties:
        embedded.containers.enabled=false
    - run Aerospike Server Docker image
    - run SpringAerospikeDataExampleApplication in debugger
    - send the request:
        curl -X POST   -H 'Content-Type: application/json'   -d '{
                "id": "id1",
                "firstName": "name1"
    }'   localhost:8080/sync/customer
*/

    public static void main(String[] args) {
        SpringApplication.run(SpringDataAerospikeExampleApplication.class, args);
    }
}
