package com.daya.android.util;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by shhong on 2017. 9. 25..
 */

public class Json {
    private final Object mJsonObject;

    public Json() {
        this.mJsonObject = new JSONObject();
    }

    public Json(@NonNull JSONObject jsonObject) throws JSONException {
        this.mJsonObject = jsonObject;
    }

    public Json(@NonNull JSONArray jsonArray) throws JSONException {
        this.mJsonObject = jsonArray;
    }

    public Json(@NonNull Map<String, Object> map) throws JSONException {
        this(toJsonObject(map));
    }

    public Json(@NonNull List<Object> list) throws JSONException {
        this(toJsonArray(list));
    }

    public Json(@NonNull String jsonString) throws JSONException {
        this.mJsonObject = new JSONObject(jsonString);
    }

    private static JSONObject toJsonObject(@NonNull Map map) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        for (Object key : map.keySet()) {
            jsonObject.put(key.toString(), toJson(map.get(key)));
        }
        return jsonObject;
    }

    private static JSONArray toJsonArray(@NonNull List<Object> list) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Object value : list) {
            jsonArray.put(toJson(value));
        }
        return jsonArray;
    }

    private static Object toJson(Object object) throws JSONException {
        if (object instanceof Map) {
            return toJsonObject((Map) object);
        } else if (object instanceof List) {
            return toJsonArray((List) object);
        } else {
            return object;
        }
    }

    public Map<String, Object> toMap() throws JSONException {
        return toMap((JSONObject) mJsonObject);
    }

    public List<Object> toList() throws JSONException {
        return toList((JSONArray) mJsonObject);
    }

    private static Map<String, Object> toMap(JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(jsonObject.get(key)));
        }
        return map;
    }

    private static List<Object> toList(JSONArray jsonArray) throws JSONException {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(fromJson(jsonArray.get(i)));
        }
        return list;
    }

    private static Object fromJson(Object object) throws JSONException {
        if (object == JSONObject.NULL) {
            return null;
        } else if (object instanceof JSONObject) {
            return toMap((JSONObject) object);
        } else if (object instanceof JSONArray) {
            return toList((JSONArray) object);
        } else {
            return object;
        }
    }

    @Override
    public String toString() {
        return mJsonObject.toString();
    }
}
