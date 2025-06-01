package org.example;


import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import io.grpc.CallOptions;
import io.grpc.MethodDescriptor;
import io.grpc.stub.AbstractStub;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static io.grpc.MethodDescriptor.generateFullMethodName;

public class GrpcDynamicService {

    public io.grpc.Channel mChannel;

    public GrpcDynamicService(io.grpc.Channel channel) {
        this.mChannel = channel;
    }


    MethodDescriptor buildMethod(String serviceName, String methodName, Message request, Message response) {

        //Create RPC method desc.
        //Only unary methods are considered as a demo.
        return io.grpc.MethodDescriptor.<Message, Message>newBuilder()
                .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                .setFullMethodName(generateFullMethodName(serviceName, methodName))
                .setSampledToLocalTracing(true)
                .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                        request))
                .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                        response))
                //add dedicated schema, which include the method full name "servicename.methodname"
                .setSchemaDescriptor(new CustomMethodDescriptorSupplier(serviceName, methodName))
                .build();

    }

    //Create a dedicated RPC method schema with service name and method name.
    private static final class CustomMethodDescriptorSupplier
            extends CustomBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
        private final java.lang.String methodName;

        CustomMethodDescriptorSupplier(String serviceName, String methodName) {
            super(serviceName);
            this.methodName = methodName;
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(methodName);
        }
    }

    //Flexiable service Descriptor
    private static class CustomBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
        String mServiceName;

        public CustomBaseDescriptorSupplier(String serviceName) {
            this.mServiceName = serviceName;
        }
        @java.lang.Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return DescriptorProtos.FileDescriptorProto.getDescriptor().getFile();
        }
        @java.lang.Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName(mServiceName);
        }
    }

    //Use grpc to call the method, only blockingUnaryCall is demostrated here.
    Object call(MethodDescriptor methodDescriptor, com.google.protobuf.Message request, CallOptions callOptions) {
        return io.grpc.stub.ClientCalls.blockingUnaryCall(mChannel, methodDescriptor, callOptions, request);
    }
}
