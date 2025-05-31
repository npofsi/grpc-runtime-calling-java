package org.example;

import com.github.os72.protobuf.dynamic.DynamicSchema;
import com.github.os72.protobuf.dynamic.MessageDefinition;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Message;
import com.google.protobuf.Struct;
import io.grpc.MethodDescriptor;
import io.grpc.protobuf.ProtoUtils;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, Descriptors.DescriptorValidationException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.

//        String jsonStr = "{ HelloRequest:{ name: 1}  }";
//        Message.Builder pbuilder = ProtobufFactory.JsonString2Protobuf(jsonStr);

//        System.out.printf(pbuilder.build().toString());
// Create dynamic schema
        DynamicSchema.Builder schemaBuilder = DynamicSchema.newBuilder();
        schemaBuilder.setName("PersonSchemaDynamic.proto");

        MessageDefinition msgDef = MessageDefinition.newBuilder("Person") // message Person
                .addField("required", "int32", "id", 1)		// required int32 id = 1
                .addField("required", "string", "name", 2)	// required string name = 2
                .addField("optional", "string", "email", 3)	// optional string email = 3
                .build();

        schemaBuilder.addMessageDefinition(msgDef);
        DynamicSchema schema = schemaBuilder.build();

// Create dynamic message from schema
        DynamicMessage.Builder msgBuilder = schema.newMessageBuilder("Person");
        Descriptors.Descriptor msgDesc = msgBuilder.getDescriptorForType();
        DynamicMessage msg = msgBuilder
                .setField(msgDesc.findFieldByName("id"), 1)
                .setField(msgDesc.findFieldByName("name"), "Alan Turing")
                .setField(msgDesc.findFieldByName("email"), "at@sis.gov.uk")
                .build();
        System.out.println(msgDef);
        System.out.println(msg);
    }
















//
}