package com.tjetchy.CheckingOutRestTestClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

    @GetMapping
    public Mono<List<Todo>> findAll() {
       return Mono.just(List.of(
               new Todo(1L, "Lear C#", false),
               new Todo(2L, "Learn python", true),
               new Todo(3L, "Learn java", true)
       ));
    }

    @GetMapping("/{id}")
    public Mono<Todo> findById(@PathVariable long id) {
        return Mono.just(new Todo(100L, "Learn C#", false));
    }
}
