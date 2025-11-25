package com.tjetchy.CheckingOutRestTestClient.service;



import com.tjetchy.CheckingOutRestTestClient.entity.Todo;
import com.tjetchy.CheckingOutRestTestClient.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(TodoServiceImpl.class);

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public Flux<Todo> findAllTodos() {
        return this.todoRepository.findAll();
    }

    @Override
    public Mono<Todo> addTodo(Todo todo) {
        return this.todoRepository.save(todo);
    }

    @Override
    public Mono<Todo> findTodoById(String id) {
        //first check if it exists else throw a run time exception
        return this.todoRepository.findById(id)
                .doOnNext(todo -> LOGGER.info("Found todo with id {}", id))
                .switchIfEmpty(Mono.defer(() -> {
                    LOGGER.warn("Todo not found with id {}", id);
                    return Mono.empty();
                }));
    }

    @Override
    public Mono<Void> deleteTodoById(String id) {
        return this.todoRepository.findById(id)
                .flatMap(todo -> {
                    LOGGER.info("Deleting todo with id {}", id);
                    return todoRepository.delete(todo)
                            .doOnSuccess(unused -> LOGGER.info("Successfully deleted todo with id {}", id));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    LOGGER.error("Todo not found with id {}", id);
                    return Mono.empty();
                }));
    }

    @Override
    public Mono<Todo> updateTodoById(String id, Todo updatedTodo) {
        return this.todoRepository.findById(id)
                .flatMap(existingTodo -> {
                    existingTodo.setTitle(updatedTodo.getTitle());
                    existingTodo.setCompleted(updatedTodo.isCompleted());
                    LOGGER.info("Updating Todo with id {}", id);
                    return this.todoRepository.save(existingTodo)
                            .doOnSuccess(saved -> LOGGER.info("Successfully updated todo with id {}", id));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    LOGGER.error("Todo is not found with id {}", id);
                    return Mono.empty();
                }));
    }
}
