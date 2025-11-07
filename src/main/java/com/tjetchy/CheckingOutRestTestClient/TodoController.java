package com.tjetchy.CheckingOutRestTestClient;

import com.tjetchy.CheckingOutRestTestClient.config.TodoPropertiesConfig;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.endpoint.baseurl:/api/v1/todo}")
public class TodoController {

    private final List<Todo> todos;

    public TodoController(TodoPropertiesConfig todoPropertiesConfig) {
        /**
         * //create a mutable copy so we can modify using ArrayList.
         * ArrayList object is mutable as against List object.
         * getItems and store in a mutable list
         */

        this.todos = new ArrayList<>(todoPropertiesConfig.getItems());
    }

    @GetMapping
    public Flux<Todo> findAll() {
       return Flux.fromIterable(todos);
    }

    @GetMapping("/{id}")
    public Mono<Todo> findById(@PathVariable Long id) {
        return todos.stream()
                .filter(todo -> todo.id().equals(id))
                .findFirst()
                .map(Mono::just)
                .orElse(Mono.empty());
    }

    @PostMapping
    public Mono<Todo> addTodo(@RequestBody Todo newTodo){
        todos.add(newTodo);
        return Mono.just(newTodo);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTodo(@PathVariable Long id){
        todos.removeIf(t -> t.id().equals(id));
        return Mono.empty();
    }

    @PutMapping("/{id}")
    public Mono<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo){
        //first check if id exists
        Optional<Todo> existingTodo = todos
                .stream()
                .filter(t -> t.id().equals(id)).findFirst();
        if (existingTodo.isEmpty())
            return Mono.empty();
        todos.remove(existingTodo.get());
        todos.add(updatedTodo);
        return Mono.just(updatedTodo);
    }
}
