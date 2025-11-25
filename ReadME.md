RestTestClient is used for WebMvc (servlet).
-------------------------------------------------------------
WebTestClient is used for WebFlux (Reactive).
---------------------------------------------------------------
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
    Documentation page: https://docs.spring.io/spring-framework/reference/testing/webtestclient.html
--------------------------------------------------------------------------------------
Delete git branch in case of mistake:
# Delete the branch locally
git branch -d branch-name

# If the branch hasn't been merged, use force delete
git branch -D branch-name

# Delete the branch remotely (if already pushed)
git push origin --delete branch-name

MongoDB can only autogenerate the following IDType:

String	✅ Yes	Uses ObjectId
-----------------------------
ObjectId✅ Yes	Native MongoDB type
-----------------------------------
Long	❌ No	You must set it manually
----------------------------------------
Integer	❌ No	You must set it manually
----------------------------------------
UUID	❌ No	You must set it manually
----------------------------------------

docker-compose down -v.
------------------------
docker-compose up --build.
-------------------------
Very useful: https://medium.com/norsys-octogone/a-local-environment-for-mongodb-with-docker-compose-ba52445b93ed
----------------------------------------------------------------------------------------------------------------

# Admin User(root user) vs Normal user (e.g., tjtechy) in mongodb

1. Admin (root user):
    This user is created by Docker when you define:
       *MONGO_INITDB_ROOT_USERNAME: root*
       *MONGO_INITDB_ROOT_PASSWORD: rootpass*
    It is created in the Admin Db.
    Has full privileges across all DBs
    Can create database, users, roles and run administrative commands
    Can authenticate with: --authenticationDatabase admin
    This user is not stored in the init.js script, MONGODB automatically
    creates it before executing the .js init script'

2. Normal User (tjetechy):
    It is created inside the init script.
    Lives in the todosdb database, NOT in admin.
    Has limited permissions (read/write only on todosdb)
    Cannot read or write other databases
    Cannot run admin commands such as:
    db.adminCommand('ping')
    listing all databases
    creating other users
    modifying indexes globally
    Can only authenticate using:
    mongodb://tjtechy:apppass@…/?authSource=todosdb

-----------------------------------------------------------------------------------------------------------------------
USEFUL STEPS:
1. docker exec -it mongodb mongosh -u admin -p password --authenticationDatabase admin
2. use <dbname> -->switched to the db
3. db.getUsers()
4. docker exec -it mongodb mongosh -u tjtechy -p apppass --authenticationDatabase todosdb todosdb --eval "db.todos.find().
----------------------------------------------------------------------------------------------
FEW TIPS ON MONGODB and Spring boot 4.0.0-rc1 bug? This is not confirmed yet.
Dependencies for running integration test using test containers.
Check pom.xml.
To make doc looks more pretty, right click and check *Render All Doc Comments*.


