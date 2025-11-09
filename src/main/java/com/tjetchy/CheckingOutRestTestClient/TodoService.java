package com.tjetchy.CheckingOutRestTestClient;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.tjetchy.CheckingOutRestTestClient.config.TodoPropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TodoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoService.class);
    private final File file; //represents actual file on the disk e.g., todos.json or todo.xml
    private final ObjectMapper objectMapper;//object that knows how to read and write data (JSON or XML)

    /**
     * NOTE: You cannot write into src/main/resources (anything under resources at run time).
     * Once Spring Boot App is packaged into JAR, everything under resources is bundled inside the JAR, which is read only
     * archive. You can read todo.json from there like (default data) but cannot write or create todo.json inside that
     * folder while app is running.
     */
    public TodoService(TodoPropertiesConfig todoPropertiesConfig) {
        this.file = new File(todoPropertiesConfig.getPath());
        this.objectMapper = todoPropertiesConfig.getFormat().equalsIgnoreCase("xml")
                ? new XmlMapper()
                : new ObjectMapper();

        ensureFileExists();
    }

    /**
     * loads todos from the JSON or XML file.
     * Returns an empty list if file is empty or missing
     * This is still not fully reactive because file is being read in a
     * blocking way.
     * @return
     */
    public Flux<Todo> loadTodos(){
       return Mono.fromCallable(() -> {

           return objectMapper.readValue(file, new TypeReference<List<Todo>>() {});
       })
               .flatMapMany(Flux::fromIterable).onErrorResume(e -> {
           if(e instanceof FileNotFoundException){
               LOGGER.info("Todos file not found");
               return Mono.empty();
           }
           LOGGER.error("Error reading todos from {}", file.getAbsolutePath(), e);
           return Flux.error(e); //can also return Flux.empty()
       })
               .subscribeOn(Schedulers.boundedElastic());
    }
    

    /**
     * saves todos back to the JSON or XML file (pretty printed)
     * @param todos
     */
    public Mono<Void> saveTodos(List<Todo> todos){
      return Mono.fromCallable(()-> {
          
          try{
              objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, todos);
          }catch (Exception e){
              LOGGER.error("Error saving todos to {}", file.getAbsolutePath(), e);
              throw new RuntimeException("Failed to save todos", e);
          }
          return null;
      }).subscribeOn(Schedulers.boundedElastic()).then();
    }


    /**
     *creates the file if it does not exist and add an empty list
     * We do not need to make this reactive since it only ensures
     * file exists
     */
    private void ensureFileExists() {
        try {

            if (!file.exists()) {
                //create parent directory if needed
                File parent = file.getParentFile();
                if(parent != null && !parent.exists()){
                    parent.mkdirs(); //ensure data directory exists
                }

                //create empty list and write initial file
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, new ArrayList<Todo>());
                LOGGER.info("Created new {} in {}", file.getName(), file.getAbsolutePath());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not create initial todos file", e);
        }

    }
}
