package com.example.moove.utilities;

import java.util.Map;

public class JsonFormatter {
    public static String jsonString(Map<String, Object> dataMap) {
        StringBuilder builder = new StringBuilder("{\n");
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            builder.append("\t").append(entry.getKey()).append(": ")
                .append(entry.getValue().toString()).append(",\n");
        }
        builder.append("}");

        return builder.toString();
    }
}
