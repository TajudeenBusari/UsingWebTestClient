package com.tjetchy.CheckingOutRestTestClient.mapper;

import com.tjetchy.CheckingOutRestTestClient.entity.AddToRequest;
import com.tjetchy.CheckingOutRestTestClient.entity.Todo;
import com.tjetchy.CheckingOutRestTestClient.entity.TodoDto;
import com.tjetchy.CheckingOutRestTestClient.entity.UpdateTodoRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class TodoMapper {


    /**
     * mapping to todoDto in reactive context
     * @param todo
     * @return
     */
    public static Mono<TodoDto> mapTodoToDto(Mono<Todo> todo){
        return todo.flatMap(t -> Mono.just(new TodoDto(
                t.getId(),
                t.getTitle(),
                t.isCompleted())));
    }

    /**
     * mapping to the dto in a blocking logic
     * @param todo
     * @return
     */
    public static TodoDto mapTodoToDtoNonReactively(Todo todo){
        return new TodoDto(todo.getId(), todo.getTitle(), todo.isCompleted());
    }


    /**
     * Reactive mapping from Todos to Dtos
     * @param todos
     * @return
     */
    public static Flux<TodoDto> mapTodosToDtos(Flux<Todo> todos){
        return todos.map(TodoMapper::mapTodoToDtoNonReactively);

    }

    public static Todo mapFromAddRequestTodo(AddToRequest toRequest){
        return new Todo(
                null,
                toRequest.title(),
                toRequest.completed()
        );
    }

    public static Todo mapFromUpdateToTodo(UpdateTodoRequest updateRequest){
        return new Todo(
                null,
                updateRequest.title(),
                updateRequest.completed()
        );
    }
}
