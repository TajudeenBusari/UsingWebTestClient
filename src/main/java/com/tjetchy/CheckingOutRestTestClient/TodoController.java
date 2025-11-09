package com.tjetchy.CheckingOutRestTestClient;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


import java.util.List;


@RestController
@RequestMapping("${api.endpoint.baseurl:/api/v1/todo}")
public class TodoController {

   private final TodoService todoService;

    public TodoController(TodoService todoService) {
       this.todoService = todoService;
    }

    @GetMapping
    public Flux<Todo> findAll() {
        return todoService.loadTodos();
    }

    @GetMapping("/{id}")
    public Mono<Todo> findById(@PathVariable Long id) {
        return todoService.loadTodos()
                .filter(todo -> todo.id().equals(id))
                .next() //take the first matching element, returns Mono<Todo>
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Todo not found" + id
                )));
    }

    @PostMapping
    public Mono<Todo> addTodo(@RequestBody Todo newTodo){
        return this.todoService.loadTodos()
                .collectList()
                .flatMap(existingTodos -> {
                    //generate new Id
                    long newId = existingTodos.stream()
                            .mapToLong(Todo::id)
                            .max()
                            .orElse(0L) + 1;
                    //create a new record with the generated ID
                    Todo todoWithId = new Todo(newId, newTodo.title(), newTodo.completed());
                    existingTodos.add(todoWithId);
                    return this.todoService.saveTodos(existingTodos)
                            .thenReturn(todoWithId);
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTodo(@PathVariable Long id){
      return this.todoService.loadTodos()
              .collectList()//converts the Flux<Todo> to Mono<List<Todo>>
              .flatMap(existingTodos -> {
                  //filter out what to delete
                  List<Todo> updatedTodos = existingTodos.stream()
                          .filter(t -> !t.id().equals(id))
                          .toList();
                  //save the updated list without the id
                  return this.todoService.saveTodos(updatedTodos);

              }).subscribeOn(Schedulers.boundedElastic()); //tells Reactor to run this whole reactive chain on a thread pool suitable for blocking I/O.
    }

    @PutMapping("/{id}")
    public Mono<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo){

        return this.todoService.loadTodos()
                .collectList() //Flux<Todo> -> Mono<List<Todo>>
                .flatMap(existingTodos -> {
                    //Check if the Todo exist with stream
                    boolean exists = existingTodos.stream().anyMatch(t -> t.id().equals(id));
                    if(!exists){
                        return Mono.empty();
                    }
                    //build the new list
                    List<Todo> updatedList = existingTodos.stream()
                            .map(t -> t.id().equals(id)
                            ? new Todo(id, updatedTodo.title(), updatedTodo.completed())
                                    : t)
                            .toList();
                    //save and return the updated Todo
                    return this.todoService.saveTodos(updatedList)
                            .thenReturn(updatedTodo);
                }).subscribeOn(Schedulers.boundedElastic());
    }
}
