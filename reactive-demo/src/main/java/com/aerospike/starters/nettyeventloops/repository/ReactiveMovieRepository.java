package com.aerospike.starters.nettyeventloops.repository;

import com.aerospike.starters.nettyeventloops.entity.MovieDocument;
import org.springframework.data.aerospike.repository.ReactiveAerospikeRepository;

public interface ReactiveMovieRepository extends ReactiveAerospikeRepository<MovieDocument, String> {

}
