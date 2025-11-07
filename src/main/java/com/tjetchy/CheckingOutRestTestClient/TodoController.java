package com.tjetchy.CheckingOutRestTestClient;

import com.tjetchy.CheckingOutRestTestClient.config.TodoPropertiesConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}")
public class TodoController {
    private final TodoPropertiesConfig todoPropertiesConfig;

    public TodoController(TodoPropertiesConfig todoPropertiesConfig) {
        this.todoPropertiesConfig = todoPropertiesConfig;
    }

    @GetMapping
    public Mono<List<Todo>> findAll() {
       return Mono.just(todoPropertiesConfig.getItems());
    }

    @GetMapping("/{id}")
    public Mono<Todo> findById(@PathVariable long id) {
        return Mono.just(todoPropertiesConfig.getItems().get(0));
    }
}
