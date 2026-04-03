package com.aerospike.starters.nettyeventloops.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.aerospike.mapping.Document;
import org.springframework.data.aerospike.mapping.Field;
import org.springframework.data.annotation.Id;

@Data
@Document(collection = "demo-reactive-nettyeventloops-set")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MovieDocument {

    @Id
    String id;

    @Field
    String name;

    @Field("desc")
    String description;

    @Field
    int likes;

    @Field
    double rating;
}
