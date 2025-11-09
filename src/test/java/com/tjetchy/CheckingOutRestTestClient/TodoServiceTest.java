package com.tjetchy.CheckingOutRestTestClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.tjetchy.CheckingOutRestTestClient.config.TodoPropertiesConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    TodoPropertiesConfig todoPropertiesConfig;

    private TodoService todoService;

    private List<Todo> todos;

    private Path tempFile;


    @BeforeEach
    void setUp() {
        todos = new ArrayList<>(); //initialize with empty list
        todos.add(new Todo(1L, "Learn C#", false));
        todos.add(new Todo(2L, "Learn C++", false));
        todos.add(new Todo(3L, "Learn Python", true));
    }

    @AfterEach
    void tearDown() throws IOException {
        //clean up the temporary file
        if (tempFile != null) {
            Files.deleteIfExists(tempFile);
        }
    }

    private void setUpTestFile(String format) throws IOException {
        String fileExtension = format.equalsIgnoreCase("xml") ? "xml" : "json";
        tempFile = Files.createTempFile("test-todos", "." + fileExtension);

        //write test data in the appropriate format
        if(format.equalsIgnoreCase("xml")) {
            XmlMapper xmlMapper = new XmlMapper();
            String xml = xmlMapper.writeValueAsString(todos);
            Files.writeString(tempFile, xml);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(todos);
            Files.writeString(tempFile, json);
        }
        when(todoPropertiesConfig.getPath()).thenReturn(tempFile.toString());
        when(todoPropertiesConfig.getFormat()).thenReturn(format);
        todoService = new TodoService(todoPropertiesConfig);
    }

    @Test
    void loadTodos_JsonFormatSuccess() throws Exception {
        setUpTestFile("todos.json");
        StepVerifier.create(todoService.loadTodos())
                .expectNextMatches(t -> t.title().equals("Learn C#"))
                .expectNextMatches(t -> t.title().equals("Learn C++"))
                .expectNextMatches(t -> t.title().equals("Learn Python"))
                .verifyComplete();
    }

    @Test
    void loadTodos_XmlFormatSuccess() throws Exception {
        setUpTestFile("todos.xml");
        StepVerifier.create(todoService.loadTodos())
                .expectNextMatches(t -> t.title().equals("Learn C#"))
                .expectNextMatches(t -> t.title().equals("Learn C++"))
                .expectNextMatches(t -> t.title().equals("Learn Python"))
                .verifyComplete();
    }

    @Test
    void saveTodos_JsonFormatSuccess() throws Exception {
        setUpTestFile("todo.json");
        StepVerifier.create(todoService.saveTodos(todos))
                .verifyComplete();
        var fileContent = Files.readString(tempFile);
        assertFalse(fileContent.isEmpty());
        assertTrue(fileContent.contains("Learn C#"));
        assertTrue(fileContent.contains("Learn C++"));
        assertTrue(fileContent.contains("Learn Python"));
    }

    @Test
    void saveTodos_XmlFormatSuccess() throws Exception {
        setUpTestFile("todo.xml");
        StepVerifier.create(todoService.saveTodos(todos))
                .verifyComplete();
        var fileContent = Files.readString(tempFile);
        assertFalse(fileContent.isEmpty());
        assertTrue(fileContent.contains("Learn C#"));
        assertTrue(fileContent.contains("Learn C++"));
        assertTrue(fileContent.contains("Learn Python"));
    }
}