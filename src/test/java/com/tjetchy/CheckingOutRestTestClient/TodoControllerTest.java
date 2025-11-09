package com.tjetchy.CheckingOutRestTestClient;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Mockito.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * For the tests (add, remove and update) that lead to state change, in addition to mocking the
 * loadTodos, saveTodos must also be mocked. It does not really matter if
 * the return type of the save method return empty (List of Todos can be empty).
 * Tests only focus on the happy path.
 */
class TodoControllerTest {

    private WebTestClient webTestClient;
    private String baseUrl;
    private TodoService todoService;
    List<Todo> todos;

    @BeforeEach
    void setUp() {
        //Mock the file service so no actual file I/0 happens
        todoService = Mockito.mock(TodoService.class);
        TodoController controller = new TodoController(todoService);
        webTestClient = WebTestClient.bindToController(controller).build();
        baseUrl = "/api/v1/todo";
        todos = new ArrayList<>();
        todos.add(new Todo(1L, "Learn C#", false));
        todos.add(new Todo(2L, "Learn C++", false));
        todos.add(new Todo(3L, "Learn Python", true));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
        List<Todo> todoList = new ArrayList<>();
        todoList.add(new Todo(1L, "Learn C#", false));
        todoList.add(new Todo(2L, "Learn C++", false));
        todoList.add(new Todo(3L, "Learn Python", true));

       when(todoService.loadTodos()).thenReturn(Flux.fromIterable(todoList));

        List<Todo> todos = webTestClient.get()
                .uri(baseUrl)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<Todo>>() {})
                .returnResult()
                .getResponseBody();
        assertNotNull(todos);
        assertEquals(3, todos.size());
        assertEquals("Learn Python", todos.get(2).title());
        assertTrue(todos.get(2).completed());
    }

    @Test
    void findById() {
        //loads todos first
        when(todoService.loadTodos()).thenReturn(Flux.fromIterable(todos));
        Todo todo = webTestClient.get()
                .uri(baseUrl + "/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Todo>() {})
                .returnResult()
                .getResponseBody();
        assertEquals(1, todo.id());
        assertEquals("Learn C#", todo.title());
        assertFalse(todo.completed());
    }

    @Test
    void addTodo() {
        when(todoService.loadTodos()).thenReturn(Flux.fromIterable(todos));
        when(todoService.saveTodos(anyList())).thenReturn(Mono.empty());
        Todo todoToAdd = new Todo(0L, "Learn Accounting", true); //id will be generated automatically
        Todo todo = webTestClient.post()
                .uri(baseUrl)
                .bodyValue(todoToAdd)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Todo>(){})
                .returnResult()
                .getResponseBody();
        assertNotNull(todo.id());
        assertEquals("Learn Accounting", todo.title());
        assertTrue(todo.completed());
    }


    @Test
    void updateTodo() {
        when(todoService.loadTodos()).thenReturn(Flux.fromIterable(todos));
        when(todoService.saveTodos(anyList())).thenReturn(Mono.empty());
        Todo updatedTodo = new Todo(3L, "Learn java updated", true);
        var todo = webTestClient.put()
                .uri(baseUrl + "/{id}", 3L)
                .bodyValue(updatedTodo)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Todo>() {
                })
                .returnResult()
                .getResponseBody();
        assertEquals(todo.title(), updatedTodo.title());
    }

    @Test
    void deleteTodo() {
        when(todoService.loadTodos()).thenReturn(Flux.fromIterable(todos));
        when(todoService.saveTodos(anyList())).thenReturn(Mono.empty());
        webTestClient.delete()
                .uri(baseUrl + "/{id}", 1L)
                .exchange()
                .expectStatus().isOk();

    }
}