package com.tjetchy.CheckingOutRestTestClient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This class acts as a typed configuration holder- a simple POJO
 * that Spring automatically fills with values from application.yml file
 * @ConfigurationProperties: tells Spring to take all properties under todo.file in
 * application.yml and bind them to this object.
 * @Component: tells Spring Boot to make this class a Spring Bean and put it in the
 * application context. So it is possible to inject it elsewhere, like the TodoService.
 *
 * Read more in the ReadMe file
 */
@Component
@ConfigurationProperties(prefix = "todo.file")
public class TodoPropertiesConfig {

    private String path;
    private String format = "json"; //default if nothing is stated in the application.yml file.

    public String getPath() {
        return path; //config.getPath();   // returns "data/todo.json"
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFormat() {
        return format; //config.getFormat(); // returns "json" or "xml"
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
