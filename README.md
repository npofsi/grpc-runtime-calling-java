# grpc-runtime-calling-java

 This repository contains a simple example of how to call a Java gRPC service at runtime.

## Prerequisites

- Java 1.8
- Maven 4.0.0

## Structure

- `src/main/java/org/example/GrpcServer.java`: The gRPC server implementation.

- `src/main/java/org/example/GrpcClientPrimitive.java`: A gRPC client that uses generated classes to call the server.
- `src/main/java/org/example/GrpcClient.java`: A gRPC client that constructs the service and method calls at runtime without using generated classes.
- `src/main/proto/server.proto`: The Protocol Buffers definition for the gRPC service.
- `src/main/java/org/example/protobuf`(after build): Contains the generated Java classes from the Protocol Buffers definition.
- `pom.xml`: The Maven configuration file for building the project.

## Build the project

Run mvn package command.

```bash
mvn clean package
```

## Run the project

### Example Server

use `org.example.GrpcServer` as the main class to run the example server.

### Example Client(Compiled Protobuf, to verify the server)

use `org.example.GrpcClientPrimitive` as the main class to run the example client.

### Example Client(Using Runtime Construction)

use `org.example.GrpcClient` as the main class to run the client created at runtime.

>The `org.example.Main` class is not used.


## Runtime Calling for gRPC

The code in `org.example.GrpcClient` demonstrates how to call a gRPC service at runtime without using the generated classes.


