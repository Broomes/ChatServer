# Websocket chat backend

Websocket micro-service for basic chat app backend that accepts and broadcast JSON formatted messages.

### Prerequisites

- Java JDK 8 or higher
- Tomcat 8 or higher
- Maven

## Deployment

1. Generate .war file
```
mvn clean install
```
2. Deploy to Tomcat server (or equivalent).
3. Check that you can access the data by navigating to ../chat/{roomName}/{userName} in your web browser (or frontend).
>Note: An example would be 
```
ws://localhost:8080/ChatServer/chat/cars/bobby
```

## Built With

* [JavaEE](https://www.oracle.com/technetwork/java/javaee/overview/index.html) - The Java framework used
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Granville Broomes** - *Creator* - [Broomes](https://github.com/Broomes)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details