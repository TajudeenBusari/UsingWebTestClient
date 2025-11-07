package com.tjetchy.CheckingOutRestTestClient;

import com.tjetchy.CheckingOutRestTestClient.config.TodoPropertiesConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoControllerTest {
    private WebTestClient webTestClient;
    //private RestTestClient restTestClient;

    @BeforeEach
    void setUp() {
        TodoPropertiesConfig todoPropertiesConfig = new TodoPropertiesConfig();
        todoPropertiesConfig.setItems(List.of(
               new Todo(1L, "Learn C#", false),
                new Todo(2L, "Learn python", true),
                new Todo(3L, "Learn java", false)
        ));
        //initialize and bind to RestTestClient to controller
        webTestClient = WebTestClient.bindToController(
                new TodoController(todoPropertiesConfig)
        ).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
        List<Todo> todos = webTestClient.get()
                .uri("/api/v1/todo")
                .exchange()

                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<Todo>>() {})
                .returnResult()
                .getResponseBody();
        assertNotNull(todos);
        assertEquals(3, todos.size());
        assertEquals("Learn java", todos.get(2).title());
        assertFalse(todos.get(2).completed());
    }

    @Test
    void findById() {
        Todo todo = webTestClient.get()
                .uri("/api/v1/todo/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Todo>() {})
                .returnResult()
                .getResponseBody();
        assertEquals(1, todo.id());
        assertEquals("Learn C#", todo.title());
        assertFalse(todo.completed());


    }
}