package com.hl.stock.common.util;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * json工具
 */
public class JsonUtils {
    // 对象属性 discountPrice转为discount_price
    private static final Gson gson = createGson();
    private static final Gson prettyGson = createPrettyGson();

    protected static Gson createGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

    protected static Gson createPrettyGson() {
        return createGson().newBuilder().setPrettyPrinting().create();
    }

    /**
     * 对象转json
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * 对象转json并美化
     *
     * @param obj
     * @return
     */
    public static String toPrettyJson(Object obj) {
        return prettyGson.toJson(obj);
    }

    /**
     * json转对象
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }

    public static <T> T fromJson(String json, Type type, FieldNamingPolicy fieldNamingPolicy) {
        return gson.newBuilder().setFieldNamingPolicy(fieldNamingPolicy).create().fromJson(json, type);
    }

    /**
     * 美化json
     *
     * @param json
     * @return
     */
    public static String prettyJson(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
        return prettyGson.toJson(jsonObject);
    }
}
