package com.pay.server.bean;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Json {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Json.class);
    public static <T extends Json> T optObj(Class<T> c, JSONObject obj) {
        if (obj != null) {
            try {
                T t = c.newInstance();
                t.init(obj);
                return t;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    protected abstract void init(JSONObject obj);
    public static <T extends Json> List<T> optList(Class<T> c, JSONArray array) {
        if (array != null) {
            List<T> list = new ArrayList<T>();
            for (int i = 0; i < array.length(); i++) {
                T t = optObj(c, array.optJSONObject(i));
                if (t != null) list.add(t);
            }
            return list;
        }
        return null;
    }
    public static void put(JSONObject obj, String name, boolean value) {
        if (value) try {
            obj.put(name, value);
        } catch (JSONException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    public static void put(JSONObject obj, String name, int value) {
        if (value != 0) try {
            obj.put(name, value);
        } catch (JSONException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    public static void put(JSONObject obj, String name, Object value) {
        if (value != null) try {
            obj.put(name, value);
        } catch (JSONException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    public static void put(JSONObject obj, String name, String value) {
        if (value != null && value.length() > 0) {
            try {
                obj.put(name, value);
            } catch (JSONException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
    public static <T extends Json> JSONArray toArray(List<T> list) throws JSONException {
        JSONArray result = null;
        if (list != null) {
            result = new JSONArray();
            for (T t : list) {
                if (t != null) result.put(t.toJson());
            }
        }
        return result;
    }
    public JSONObject toJson() {
        return new JSONObject();
    }
    public static <T extends Json> JSONObject toJson(T t) throws JSONException {
        JSONObject result = null;
        if (t != null) {
            result = t.toJson();
        }
        return result;
    }
    public static <T extends Json> JSONArray toJson(List<T> list) throws JSONException {
        JSONArray array = null;
        if (list != null) {
            array = new JSONArray();
            for (T t : list) {
                array.put(toJson(t));
            }
        }
        return array;
    }
}
