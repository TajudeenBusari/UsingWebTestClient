RestTestClient is used for WebMvc (servlet)
WebTestClient is used for WebFlux (Reactive)
Works with spring boot 4.0.0 RC1 as of today 07-11-2025
---------------------------------------------------------------
Switch between xml or json in application.yml file depending on the file format
you want to write or read data.
----------------------------------------------------------------------
@ConfigurationProperties(prefix="todo.file")
Tells Spring Boot:
    Take all properties under todo.file in application.yml and bind them to this object
    For example:
        todo:
            file:
                path: data/todo.json
                format: json
    means:
    | YAML key           | Java property | Value injected     |
    | ------------------ | ------------- | ------------------ |
    | todo.file.path    |     path       |    "data/todo.json" |
    | todo.file.format  |    format     |       "json"        |

WebTestClient has the following:
    bindToController() ---> unit test your REST endpoints
    bindToRouterFunction() ---> when using router-based handlers
    bindToApplicationContext() ---> used in integration tests (@SpringBootTest)
    bindToServer() ---> A real HTTP server, for end-to-end tests with actual networking