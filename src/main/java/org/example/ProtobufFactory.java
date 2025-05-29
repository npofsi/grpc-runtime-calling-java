package org.example;

import com.google.gson.JsonParser;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct.Builder;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import org.json.JSONObject;

public class ProtobufFactory {

    public static Builder JsonString2Protobuf(String JsonString) throws InvalidProtocolBufferException {
        JSONObject parameters = new JSONObject(JsonString);

        Builder structBuilder = Struct.newBuilder();
        JsonFormat.parser().merge(parameters.toString(), structBuilder);

        return structBuilder;
    }


}
