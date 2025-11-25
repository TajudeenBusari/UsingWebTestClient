package com.tjetchy.CheckingOutRestTestClient.controller;


import com.tjetchy.CheckingOutRestTestClient.entity.AddToRequest;
import com.tjetchy.CheckingOutRestTestClient.entity.TodoDto;
import com.tjetchy.CheckingOutRestTestClient.entity.UpdateTodoRequest;
import com.tjetchy.CheckingOutRestTestClient.mapper.TodoMapper;
import com.tjetchy.CheckingOutRestTestClient.service.TodoService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("${api.endpoint.baseurl:/api/v1/todo}")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoServiceImpl) {
        this.todoService = todoServiceImpl;
    }

    @GetMapping
    public Flux<TodoDto> findAll() {
        return TodoMapper
                .mapTodosToDtos(todoService.findAllTodos());
    }

    @GetMapping("/{id}")
    public Mono<TodoDto> findById(@PathVariable String id) {
        var todo = this.todoService.findTodoById(id);
        return TodoMapper.mapTodoToDto(todo);
    }

    @PostMapping
    public Mono<TodoDto> addTodo(@RequestBody AddToRequest newTodo) {
        //convert to Todo
        var todo = TodoMapper.mapFromAddRequestTodo(newTodo);
        return TodoMapper.mapTodoToDto(this.todoService.addTodo(todo));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTodo(@PathVariable String id) {

        return this.todoService.deleteTodoById(id);
    }

    @PutMapping("/{id}")
    public Mono<TodoDto> updateTodo(@PathVariable String id, @RequestBody UpdateTodoRequest updatedTodo) {
        //map to todo
        var todo = TodoMapper.mapFromUpdateToTodo(updatedTodo);
        var updated = this.todoService.updateTodoById(id, todo);
        //map to Dto
        return TodoMapper.mapTodoToDto(updated);
    }
}
