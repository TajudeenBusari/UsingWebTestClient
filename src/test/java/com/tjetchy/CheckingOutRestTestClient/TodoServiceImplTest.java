package com.tjetchy.CheckingOutRestTestClient;

import com.tjetchy.CheckingOutRestTestClient.entity.Todo;
import com.tjetchy.CheckingOutRestTestClient.repository.TodoRepository;
import com.tjetchy.CheckingOutRestTestClient.service.TodoServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {
    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoServiceImpl;

    private List<Todo> todos;

    @BeforeEach
    void setUp() {
        ObjectId id = new ObjectId();
        String objectIdString = id.toHexString();
        todos = new ArrayList<>(); //initialize with empty list
        todos.add(new Todo(objectIdString, "Learn C#", false));
        todos.add(new Todo(objectIdString, "Learn C++", false));
        todos.add(new Todo(objectIdString, "Learn Python", true));
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    void saveTodo_Success() {
        //Given
        given(todoRepository.save(todos.get(0))).willReturn(Mono.just(todos.get(0)));
        //When
        var todo = todoServiceImpl.addTodo(todos.get(0));
        //Then
        StepVerifier.create(todo)
                .expectNext(todos.get(0))
                .verifyComplete();
    }

    @Test
    void findAllTodos_Success() {
        //given
        // can also use:  when(todoRepository.findAll()).thenReturn(Flux.fromIterable(todos))
        given(todoRepository.findAll()).willReturn(Flux.just(todos.get(0), todos.get(1), todos.get(2)));
        //when
        var result = todoServiceImpl.findAllTodos();
        //Then
        StepVerifier.create(result)
                .expectNext(todos.get(0))
                .expectNext(todos.get(1))
                .expectNext(todos.get(2))
                .verifyComplete();
    }

    @Test
    void findTodoById_Success() {
        given(todoRepository.findById(todos.get(0).getId())).willReturn(Mono.just(todos.get(0)));
        var result = todoServiceImpl.findTodoById(todos.get(0).getId());
        StepVerifier.create(result)
                .expectNext(todos.get(0))
                .verifyComplete();
    }

    @Test
    void updateTodo_Success() {
        given(todoRepository.findById(todos.get(0).getId())).willReturn(Mono.just(todos.get(0)));
        given(todoRepository.save(todos.get(0))).willReturn(Mono.just(todos.get(0)));
        var result = todoServiceImpl.updateTodoById(todos.get(0).getId(), todos.get(0));
        StepVerifier.create(result)
                .expectNext(todos.get(0))
                .verifyComplete();
    }

    @Test
    void deleteTodoById_Success() {
        given(todoRepository.findById(todos.get(0).getId())).willReturn(Mono.just(todos.get(0)));
        when(todoRepository.delete(todos.get(0))).thenReturn(Mono.empty());
        var result  = todoServiceImpl.deleteTodoById(todos.get(0).getId());
        StepVerifier.create(result)
        .verifyComplete();
    }
}