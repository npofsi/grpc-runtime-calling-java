package org.example;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import io.grpc.MethodDescriptor;
import io.grpc.protobuf.ProtoUtils;

public class GrpcFactory {
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
//
//    static MethodDescriptor.MethodType getMethodTypeFromDesc(
//            Descriptors.MethodDescriptor methodDesc
//    ) {
//        if (!methodDesc.isServerStreaming()
//                && !methodDesc.isClientStreaming()) {
//            return MethodDescriptor.MethodType.UNARY;
//        } else if (methodDesc.isServerStreaming()
//                && !methodDesc.isClientStreaming()) {
//            return MethodDescriptor.MethodType.SERVER_STREAMING;
//        } else if (!methodDesc.isServerStreaming()) {
//            return MethodDescriptor.MethodType.CLIENT_STREAMING;
//        } else {
//            return MethodDescriptor.MethodType.BIDI_STREAMING;
//        }
//    }
}
