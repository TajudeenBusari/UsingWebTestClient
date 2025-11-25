package com.tjetchy.CheckingOutRestTestClient.service;

import com.tjetchy.CheckingOutRestTestClient.entity.Todo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Anything involving all should be Flux
 */
@Service

public interface TodoService {
    Flux<Todo> findAllTodos();
    Mono<Todo> addTodo(Todo todo);
    Mono<Todo> findTodoById(String id);
    Mono<Void> deleteTodoById(String id);
    Mono<Todo> updateTodoById(String id, Todo todo);
}
