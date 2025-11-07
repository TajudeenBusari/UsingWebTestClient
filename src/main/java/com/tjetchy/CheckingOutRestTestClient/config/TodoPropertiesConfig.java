package com.tjetchy.CheckingOutRestTestClient.config;

import com.tjetchy.CheckingOutRestTestClient.Todo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "todo")
public class TodoPropertiesConfig {
    private List<Todo> items;

    //getter
    public List<Todo> getItems() {
        return items;
    }

    //Setter
    public void setItems(List<Todo> items) {
        this.items = items;
    }

}
