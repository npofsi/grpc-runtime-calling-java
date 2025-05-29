package org.example;


import io.grpc.stub.StreamObserver;
import org.example.protobuf.HelloReply;
import org.example.protobuf.HelloRequest;
import  org.example.protobuf.ProtoRepo;
import org.example.protobuf.GreeterGrpc;
// Implementation of the gRPC service on the server-side.
public class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
        // Generate a greeting message for the original method
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();

        // Send the reply back to the client.
        responseObserver.onNext(reply);

        // Indicate that no further messages will be sent to the client.
        responseObserver.onCompleted();
    }

    @Override
    public void sayHelloAgain(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
        // Generate another greeting message for the new method.
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello again " + req.getName()).build();

        // Send the reply back to the client.
        responseObserver.onNext(reply);

        // Indicate that no further messages will be sent to the client.
        responseObserver.onCompleted();
    }
}