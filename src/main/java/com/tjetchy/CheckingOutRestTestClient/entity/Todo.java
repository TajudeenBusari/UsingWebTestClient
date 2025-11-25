package com.tjetchy.CheckingOutRestTestClient.entity;

//import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Because records are immutable, Spring cant modify fields in place. So
 * when you update a record, new instance must be created.
 * There to avoid creating new record everytime, this class will be converted to
 * normal POJO class
 * //public record Todo(
 * //        Long id, String title, boolean completed
 * //) {
 * //}
 */

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
@Document(collection = "todos")
//@Getter
//@Setter
public class Todo{
    @Id
    private String id;
    private String title;
    private boolean completed;

    public Todo(String id, String title, boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    public Todo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
