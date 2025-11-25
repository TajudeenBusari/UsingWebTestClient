db = db.getSiblingDB('todosdb');

// Create user for the todosdb database (optional)
db.createUser({
  user: 'tjtechy',
  pwd: 'apppass',
  roles: [
    {
      role: 'readWrite',
      db: 'todosdb'
    }
  ]
});

//create collections
db.createCollection('todos');

//optionally insert initial test data
db.todos.insertMany(
[
    {
        _id: ObjectId("651d3a5b8f9b7a1b2c3d4e5f"),
        title: "Learn MongoDB",
        completed: true
    },
    {
        _id: ObjectId("651d3a5b8f9b7a1b2c3d4e60"),
        title: "Learn Java",
        completed: true
    }
]);
print('Todos database initialized successfully!');