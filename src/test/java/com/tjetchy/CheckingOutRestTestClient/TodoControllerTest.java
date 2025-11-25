package com.tjetchy.CheckingOutRestTestClient;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjetchy.CheckingOutRestTestClient.controller.TodoController;
import com.tjetchy.CheckingOutRestTestClient.entity.AddToRequest;
import com.tjetchy.CheckingOutRestTestClient.entity.Todo;
import com.tjetchy.CheckingOutRestTestClient.entity.TodoDto;
import com.tjetchy.CheckingOutRestTestClient.entity.UpdateTodoRequest;
import com.tjetchy.CheckingOutRestTestClient.service.TodoService;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * All tests focus on the happy path
 */
class TodoControllerTest {

    private WebTestClient webTestClient;
    private String baseUrl;
    @Mock
    private TodoService todoServiceImpl;
    List<Todo> todos;
    @InjectMocks
    private TodoController todoController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); //initializes the Mockito annotations (@Mock, @InjectMocks, @Spy etc)
        webTestClient = WebTestClient.bindToController(todoController).build();
        baseUrl = "/api/v1/todo";
        todos = new ArrayList<>();
        ObjectId id = new ObjectId();
        String objectIdString = id.toHexString();
        todos.add(new Todo(objectIdString, "Learn C#", false));
        todos.add(new Todo(objectIdString, "Learn C++", false));
        todos.add(new Todo(objectIdString, "Learn Python", true));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
        when(todoServiceImpl.findAllTodos())
                .thenReturn(Flux.just(todos.get(0), todos.get(1), todos.get(2)));

        List<Todo> todoList = new ArrayList<>();
        todoList.add(new Todo(new ObjectId().toHexString(), "Learn C#", false));
        todoList.add(new Todo(new ObjectId().toHexString(), "Learn C++", false));
        todoList.add(new Todo(new ObjectId().toHexString(), "Learn Python", true));
         webTestClient.get()
                 .uri(baseUrl)
                 .exchange()
                 .expectStatus().isOk()
                 .expectBodyList(TodoDto.class).hasSize(3);
    }

    @Test
    void findById() {
        when(todoServiceImpl.findTodoById(anyString())).thenReturn(Mono.just(todos.get(0)));
        webTestClient.get()
                .uri(baseUrl + "/" + todos.get(0).getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TodoDto.class);
    }

    @Test
    void addTodo() throws Exception {
        var addTodoDto = new AddToRequest("", false);
        Todo savedTodo = new Todo(new ObjectId().toHexString(), "test title", false);
        TodoDto dto = new TodoDto(savedTodo.getId(), "test title", false);
        var json = new ObjectMapper().writeValueAsString(addTodoDto);

        when(todoServiceImpl.addTodo(any(Todo.class))).thenReturn(Mono.just(savedTodo));
        webTestClient.post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TodoDto.class)
                .isEqualTo(dto);
    }

    @Test
    void updateTodo() throws Exception {
        var updateTodoDto = new UpdateTodoRequest("testUpdated1", false);
        Todo saved = new Todo(new ObjectId().toHexString(), "testUpdated1", false);
        TodoDto dto = new TodoDto(saved.getId(), "testUpdated1", false);
        var json = new ObjectMapper().writeValueAsString(updateTodoDto);
        when(todoServiceImpl.updateTodoById(anyString(), any(Todo.class))).thenReturn(Mono.just(saved));
        webTestClient.put()
                .uri(baseUrl + "/2")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TodoDto.class)
                .isEqualTo(dto);
    }

    @Test
    void deleteTodo() {
        when(todoServiceImpl.deleteTodoById(anyString())).thenReturn(Mono.empty());
        webTestClient.delete()
                .uri(baseUrl + "/" + todos.get(0).getId())
                .exchange()
                .expectStatus().isOk();
    }
}