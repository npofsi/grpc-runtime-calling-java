package org.example;

import com.google.protobuf.*;
import com.google.protobuf.util.JsonFormat;
import io.grpc.*;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
//import org.example.protobuf.GreeterGrpc;
//import org.example.protobuf.HelloReply;
//import org.example.protobuf.HelloRequest;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GrpcClient {
    private static final Logger logger = Logger.getLogger(GrpcClient.class.getName());


    public static void main(String[] args) throws Exception {
        String user = "world";
        // Access a service running on the local machine on port 50051
        String target = "localhost:50051";
        // Allow passing in the user and target strings as command line arguments
        if (args.length > 0) {
            if ("--help".equals(args[0])) {
                System.err.println("Usage: [name [target]]");
                System.err.println("");
                System.err.println("  name    The name you wish to be greeted by. Defaults to " + user);
                System.err.println("  target  The server to connect to. Defaults to " + target);
                System.exit(1);
            }
            user = args[0];
        }
        if (args.length > 1) {
            target = args[1];
        }

        // Create a communication channel to the server, known as a Channel. Channels are thread-safe
        // and reusable. It is common to create channels at the beginning of your application and reuse
        // them until the application shuts down.
        //
        // For the example we use plaintext insecure credentials to avoid needing TLS certificates. To
        // use TLS, use TlsChannelCredentials instead.
        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
                .build();


        try {

            //
            //Start of building request:
            //
            //If using ProtoJson, the json message will be { username: test }
            //Define package name and message type name
            String requestPackageName = "org.example.protobuf";
            String requestMessageName = "HelloRequest";

            //Create empty field
            DescriptorProtos.FieldDescriptorProto.Builder fieldDescriptorProto = DescriptorProtos.FieldDescriptorProto.newBuilder()
                    .setName("username")
                    .setNumber(1)
                    .setType(DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING)
                    .setLabel(DescriptorProtos.FieldDescriptorProto.Label.LABEL_REQUIRED)
                    .setDefaultValue("");

            //Create message type with type name
            Descriptors.Descriptor requestDescriptor = DescriptorProtos.DescriptorProto.getDescriptor();
            DescriptorProtos.DescriptorProto.Builder requestMessageDescriptorProto = DescriptorProtos.DescriptorProto.newBuilder()
                    .setName(requestMessageName)
                    .addField(fieldDescriptorProto);

            //Create file to attach package name on message type
            DescriptorProtos.FileDescriptorProto requestFileDescriptorProto =
                    DescriptorProtos.FileDescriptorProto.newBuilder()
                            .setPackage(requestPackageName)
                            .addMessageType(requestMessageDescriptorProto)
                            .build();
            Descriptors.FileDescriptor requestFileDescriptor =
                    Descriptors.FileDescriptor.buildFrom(requestFileDescriptorProto, new Descriptors.FileDescriptor[0]);

            //Create message builder with defined message type
            DynamicMessage.Builder requestBuilder = DynamicMessage.newBuilder(requestFileDescriptor.findMessageTypeByName(requestMessageName));

            //Also can create message builder using ProtoJSON format, but ProtoJSON is lack for a precise converter with no-typed JSON
            //Do not use this method.
            //Parse Json format protobuf message and merge it to empty DynamicMessageBuilder
            //JsonFormat.parser().ignoringUnknownFields().merge(jsonStr,requestBuilder);

            //set field in message with payloads
            requestBuilder.setField(requestFileDescriptor.findMessageTypeByName(requestMessageName).findFieldByName("username"), "test");
            System.out.println(requestMessageDescriptorProto);


            //Build request message Instance
            Message request = requestBuilder.build();//HelloRequest.newBuilder().setName(name).build();
            System.out.println(request);
            //
            //Start of building response:
            //
            //Define response message using JsonFormat
            //If using ProtoJson, json message will be "{ name: null }";
            Descriptors.Descriptor responseDescriptor = DescriptorProtos.DescriptorProto.getDescriptor();// DescriptorProtos.getDescriptor().
            DynamicMessage.Builder responseBuilder = DynamicMessage.newBuilder(responseDescriptor);

//            JsonFormat.parser().ignoringUnknownFields().merge(jsonStr2,responseBuilder);
            System.out.println(responseBuilder.getAllFields());
            Message response = responseBuilder.build();

            System.out.println("Send Request:\n" + request.toBuilder().build().toString());

            GrpcDynamicService stubService = new GrpcDynamicService(channel);
            MethodDescriptor methodDescriptor = stubService.buildMethod("org.example.protobuf.Greeter", "SayHello", request, response);

            CallOptions callOptions = CallOptions.DEFAULT;
            responseBuilder.mergeFrom((Message) stubService.call(methodDescriptor, request,  callOptions));
            System.out.println("Received Response:\n" + responseBuilder.build());
//            GrpcClient client = new GrpcClient(channel);
//            client.greet(user);
        } finally {
            // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
            // resources the channel should be shut down when it will no longer be used. If it may be used
            // again leave it running.
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

//
//    private final GreeterGrpc.GreeterBlockingStub blockingStub;
//
//    /** Construct client for accessing server using the existing channel. */
//    public GrpcClient(Channel channel) {
//        // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
//        // shut it down.
//
//        // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
//        blockingStub = GreeterGrpc.newBlockingStub(channel);
//    }
//
//    /** Say hello to server. */
//    public void greet(String name) throws InvalidProtocolBufferException {
//        logger.info("Will try to greet " + name + " ...");
//
//
//        String jsonStr = "{ \"HelloRequest\":{ name: 1}  }";
//        Message.Builder pbuilder = ProtobufFactory.JsonString2Protobuf(jsonStr);
//
//
//
//        Message request = pbuilder.build();//HelloRequest.newBuilder().setName(name).build();
//        HelloReply response;
//        try {
//            response = blockingStub.sayHello(request);
//        } catch (StatusRuntimeException e) {
//            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
//            return;
//        }
//        logger.info("Greeting: " + response.getMessage());
//    }

    /**
     * Greet server. If provided, the first element of {@code args} is the name to use in the
     * greeting. The second argument is the target server.
     */

//    static MethodDescriptor from(
//            Descriptors.MethodDescriptor methodDesc
//    ) {
//        return MethodDescriptor.<DynamicMessage, DynamicMessage>newBuilder()
//                // UNKNOWN is fine, but the "correct" value can be computed from
//                // methodDesc.toProto().getClientStreaming()/getServerStreaming()
//                .setType(getMethodTypeFromDesc(methodDesc))
//                .setFullMethodName(MethodDescriptor.generateFullMethodName(
//                        serviceDesc.getFullName(), methodDesc.getName()))
//                .setRequestMarshaller(ProtoUtils.marshaller(
//                        DynamicMessage.getDefaultInstance(methodDesc.getInputType())))
//                .setResponseMarshaller(ProtoUtils.marshaller(
//                        DynamicMessage.getDefaultInstance(methodDesc.getOutputType())))
//                .build();
//    }
    static MethodDescriptor.MethodType getMethodTypeFromDesc(
            Descriptors.MethodDescriptor methodDesc
    ) {
        if (!methodDesc.isServerStreaming()
                && !methodDesc.isClientStreaming()) {
            return MethodDescriptor.MethodType.UNARY;
        } else if (methodDesc.isServerStreaming()
                && !methodDesc.isClientStreaming()) {
            return MethodDescriptor.MethodType.SERVER_STREAMING;
        } else if (!methodDesc.isServerStreaming()) {
            return MethodDescriptor.MethodType.CLIENT_STREAMING;
        } else {
            return MethodDescriptor.MethodType.BIDI_STREAMING;
        }
    }
}
