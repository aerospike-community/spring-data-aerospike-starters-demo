package com.aerospike.starters.demo;

import org.springframework.data.aerospike.repository.ReactiveAerospikeRepository;
import reactor.core.publisher.Flux;

public interface ReactiveCustomerRepository extends ReactiveAerospikeRepository<Customer, String> {
    Flux<Customer> findByLastNameOrderByFirstNameAsc(String lastName);
}
