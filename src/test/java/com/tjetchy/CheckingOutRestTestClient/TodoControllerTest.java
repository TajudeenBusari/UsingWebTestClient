package com.tjetchy.CheckingOutRestTestClient;

import com.tjetchy.CheckingOutRestTestClient.config.TodoPropertiesConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoControllerTest {
    private WebTestClient webTestClient;
    //private RestTestClient restTestClient; //used for spring mvc
    private String baseUrl;

    @BeforeEach
    void setUp() {
        TodoPropertiesConfig todoPropertiesConfig = new TodoPropertiesConfig();
        /**
         * In the controller class, the List.of() is already wrapped with a mutable ArrayList,
         * so it is going to work for updating, deleting and adding in this test class
         */
        todoPropertiesConfig.setItems(List.of(
                new Todo(1L, "Learn C#", false),
                new Todo(2L, "Learn python", true),
                new Todo(3L, "Learn java", false)
        ));

        //initialize and bind to RestTestClient to controller
        /**
         * because in the test, there is a WebTestClient binding, not a full a spring boot context,
         * Spring boot property resolution does not occur. @RequestMapping("${api.endpoint.baseurl}")
         * stays as the literal string "${api.endpoint.baseurl}".
         * When WebFlux parses it, it interprets ${...} as a path variable pattern.
         * The . inside ${api.endpoint.baseurl} causes the error: causing:
         * Char '.' is not allowed in a captured variable name.
         * The solution is to inject or initialize the base url in the controller class as:
         * @RequestMapping("${api.endpoint.baseurl:/api/v1/todo}"),
         * then in test we explicitly write the base url as follows:  baseUrl = "/api/v1/todo";
         *
         */
        webTestClient = WebTestClient.bindToController(
                new TodoController(todoPropertiesConfig)
        ).build();

        baseUrl = "/api/v1/todo";
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
        List<Todo> todos = webTestClient.get()
                .uri(baseUrl)
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
        Todo todoToAdd = new Todo(7L, "Learn Accounting", true);
        Todo todo = webTestClient.post()
                .uri(baseUrl)
                .bodyValue(todoToAdd)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Todo>(){})
                .returnResult()
                .getResponseBody();
        assertEquals(7L, todo.id());
        assertEquals("Learn Accounting", todo.title());
        assertTrue(todo.completed());
    }

    @Test
    void updateTodo() {
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
        webTestClient.delete()
                .uri(baseUrl + "/{id}", 1L)
                .exchange()
                .expectStatus().isOk();

    }
}