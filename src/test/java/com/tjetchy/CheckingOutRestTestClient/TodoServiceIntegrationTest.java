package com.tjetchy.CheckingOutRestTestClient;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.internal.bulk.UpdateRequest;
import com.tjetchy.CheckingOutRestTestClient.entity.AddToRequest;
import com.tjetchy.CheckingOutRestTestClient.entity.TodoDto;
import com.tjetchy.CheckingOutRestTestClient.entity.UpdateTodoRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;


import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureWebTestClient
@Tag("TodoIntegrationTest")
@ActiveProfiles("test")
public class TodoServiceIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;
    @Value("${api.endpoint.baseurl:/api/v1/todo}")
    private String baseUrl;
    @LocalServerPort
    private int port;

    /**
     * The @ServiceConnection tells the spring boot that:
     * The TestContainer instance is the MongoDB service and it wil be used automatically for
     * spring.daya.mongodb.* configuration.
     * The Spring boot will:
     * start the container at the right time
     * discover it is a MongoDb container
     * extract the replica-ser URI and apply automatically
     * configure the ReactiveMongoClient(in this case:
     * reactive MongoDB driver client that Spring Data MongoDB uses internally to talk to Mongo.) to use the Uri
     * So need to set the dynamic property sources and start or stop container.
     * It will start and shut down gracefully.
     * .withReplicaSet(): Spring data MongoDB requires a replica set to enable transactions and reactive features,
     * turns the container into a single-node replica set.
     */
    @Container
    @ServiceConnection // Spring Boot 4 automatically uses this container for MongoDB, no dynamic properties overriding
    private static final MongoDBContainer  mongoDBContainer =
            new MongoDBContainer("mongo:7.0.5")
                    .withReplicaSet();

    @BeforeEach
    void setup() {
        // Debug: Verify Testcontainers connection
        System.out.println("Testcontainers MongoDB URL: " + mongoDBContainer.getReplicaSetUrl());
        System.out.println("Testcontainers MongoDB Host: " + mongoDBContainer.getHost());
        System.out.println("Testcontainers MongoDB Port: " + mongoDBContainer.getFirstMappedPort());
    }

    private TodoDto createTodo(){
        var uniqueTitle = "title" + System.currentTimeMillis();
        AddToRequest newTodo = new AddToRequest(uniqueTitle, true);
        var responseBody = webTestClient.post()
                .uri(baseUrl)
                .bodyValue(newTodo)//no need for objectmapper because
                .exchange()
                .expectStatus().isOk()
                .expectBody(TodoDto.class)
                .returnResult()
                .getResponseBody();
        assertNotNull(responseBody, "Response body should not be null");
        return responseBody;
    }


    @Test
    @DisplayName("TEST CREATE TODO USING HELPER METHOD (POST /todo)")
    public void testCreateSuccessFromHelperMethod(){
        var createdTodo = createTodo();
        System.out.println("Newly created Todo Id is: " +createdTodo.id());
        System.out.println(createdTodo);
    }

    @Test
    @DisplayName("TEST CREATE TODO (POST /todo)")
    public void testCreateTodo(){
        //given
        AddToRequest newTodo = new AddToRequest(
             "Write integration Test", false
        );
        //when and then
        webTestClient.post()
                .uri(baseUrl )
                .bodyValue(newTodo)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    System.out.println("====RESPONSE_BODY======: " + new String(response.getResponseBody()));
                })
                .jsonPath("$.title").isEqualTo("Write integration Test")
                .jsonPath("$.completed").isEqualTo(false)
                .jsonPath("$.id").exists();
    }

    @Test
    @DisplayName("TEST FIND TODO BY ID (GET /todo/id)")
    public void testFindTodoById(){
        var id = createTodo().id();
        var responseBody = webTestClient.get()
                .uri(baseUrl + "/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TodoDto.class)
                .returnResult()
                .getResponseBody();
        System.out.println(responseBody);
    }

    @Test
    @DisplayName("TEST UPDATE TODO BY ID (PUT /todo/id)")
    public void testUpdateTodoById(){
        var id = createTodo().id();
        var updatedTitle = createTodo().title() + "Updated";
        var updateRequest = new UpdateTodoRequest(updatedTitle, false);
        var responseBody = webTestClient.put()
                .uri(baseUrl + "/" + id)
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TodoDto.class)
                .returnResult()
                .getResponseBody();
        System.out.println(responseBody);
    }

    @Test
    @DisplayName("TEST FIND ALL TODO (GET /todo)")
    public void testFindAllTodo(){
        createTodo();
        createTodo();

        var todos = webTestClient.get()
                .uri(baseUrl)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TodoDto.class)
                .returnResult()
                .getResponseBody();
        System.out.println(todos);
    }

    @Test
    @DisplayName("TEST DELETE TODO BY ID (DELETE /todo/id)")
    public void testDeleteTodoById(){
        var id = createTodo().id();

         webTestClient.delete()
                .uri(baseUrl + "/" + id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TodoDto.class)
                .returnResult();
    }

}
