package com.tjetchy.CheckingOutRestTestClient.repository;

import com.tjetchy.CheckingOutRestTestClient.entity.Todo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;


public interface TodoRepository extends ReactiveMongoRepository<Todo, String> {
}
