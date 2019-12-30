package com.cocos.library_base.utils.singleton;

import com.cocos.library_base.entity.js_response.JsContractParamsModel;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataTypeAdaptor extends TypeAdapter<JsContractParamsModel> {

    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (type.getRawType() == JsContractParamsModel.class) {
                return (TypeAdapter<T>) new DataTypeAdaptor(gson);
            }
            return null;
        }
    };

    private final Gson gson;

    DataTypeAdaptor(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, JsContractParamsModel value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("nameOrId");
        gson.getAdapter(String.class).write(out, value.nameOrId);
        out.name("functionName");
        gson.getAdapter(String.class).write(out, value.functionName);
        out.name("valueList");
        gson.getAdapter(List.class).write(out, value.valueList);
        out.endObject();
    }

    @Override
    public JsContractParamsModel read(JsonReader in) throws IOException {
        JsContractParamsModel data = new JsContractParamsModel();
        Map<String, Object> dataMap = (Map<String, Object>) readInternal(in);
        data.nameOrId = ((String) dataMap.get("nameOrId"));
        data.functionName = ((String) dataMap.get("functionName"));
        data.valueList = ((List) dataMap.get("valueList"));
        return data;
    }


    private Object readInternal(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        switch (token) {
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList<Object>();
                in.beginArray();
                while (in.hasNext()) {
                    list.add(readInternal(in));
                }
                in.endArray();
                return list;

            case BEGIN_OBJECT:
                Map<String, Object> map = new LinkedTreeMap<String, Object>();
                in.beginObject();
                while (in.hasNext()) {
                    map.put(in.nextName(), readInternal(in));
                }
                in.endObject();
                return map;

            case STRING:
                return in.nextString();

            case NUMBER:
                //将其作为一个字符串读取出来
                String numberStr = in.nextString();
                //返回的numberStr不会为null
                if (numberStr.contains(".") || numberStr.contains("e")
                        || numberStr.contains("E")) {
                    return Double.parseDouble(numberStr);
                }
                return Long.parseLong(numberStr);

            case BOOLEAN:
                return in.nextBoolean();

            case NULL:
                in.nextNull();
                return null;

            default:
                throw new IllegalStateException();
        }
    }
}

