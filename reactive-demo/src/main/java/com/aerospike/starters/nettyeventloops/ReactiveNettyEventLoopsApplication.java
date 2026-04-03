package com.aerospike.starters.nettyeventloops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
	org.springframework.boot.aerospike.reactive.data.AerospikeReactiveDataAutoConfiguration.class,
	org.springframework.boot.aerospike.reactive.data.AerospikeReactiveRepositoriesAutoConfiguration.class
})
public class ReactiveNettyEventLoopsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveNettyEventLoopsApplication.class, args);
	}
}
